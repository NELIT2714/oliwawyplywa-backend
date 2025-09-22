package pl.oliwawyplywa.web.services.tpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@Service
public class TpaySignatureService {

    private final TpayCertService certService;

    public TpaySignatureService(TpayCertService certService) {
        this.certService = certService;
    }

    private static String maybeTruncate(String s) {
        if (s == null) return "null";
        return s.length() <= 2000 ? s : s.substring(0, 2000) + "...(len=" + s.length() + ")";
    }

    private static String hexPrefix(byte[] b) {
        if (b == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(b.length, 80); i++) {
            sb.append(String.format("%02x", b[i]));
        }
        if (b.length > 80) sb.append("...");
        return sb.toString();
    }

    public boolean verify(String jws, byte[] rawBodyBytes) throws Exception {
        if (jws == null || jws.isEmpty()) {
            System.out.println("[VERIFY] empty jws");
            return false;
        }

        jws = jws.trim();
        String[] parts = jws.split("\\.");
        if (parts.length != 3) {
            System.out.println("[VERIFY] jws parts != 3 -> " + parts.length);
            return false;
        }

        String headerB64 = parts[0];
        String payloadB64FromJws = parts[1];
        String signatureB64 = parts[2];

        String headerJson = new String(Base64.getUrlDecoder().decode(headerB64), StandardCharsets.UTF_8);
        Map<String, Object> header = new ObjectMapper().readValue(headerJson, Map.class);

        System.out.println("[VERIFY] headerJson: " + maybeTruncate(headerJson));
        if (!header.containsKey("x5u")) {
            System.out.println("[VERIFY] header missing x5u");
            return false;
        }
        String x5u = header.get("x5u").toString();
        System.out.println("[VERIFY] x5u: " + x5u);

        // load signing cert
        byte[] certBytes = URI.create(x5u).toURL().openStream().readAllBytes();
        X509Certificate signingCert = (X509Certificate) CertificateFactory.getInstance("X.509")
            .generateCertificate(new ByteArrayInputStream(certBytes));

        // validate a chain (may throw)
        certService.verifyCertificateChain(signingCert);

        byte[] sig = Base64.getUrlDecoder().decode(signatureB64);

        // decode jws payload for inspection
        byte[] jwsPayloadBytes = Base64.getUrlDecoder().decode(payloadB64FromJws);
        String jwsPayloadString = new String(jwsPayloadBytes, StandardCharsets.UTF_8);

        System.out.println("[VERIFY] jws.payloadB64 (from header): " + payloadB64FromJws);
        System.out.println("[VERIFY] jws.payload (decoded UTF-8 preview): " + maybeTruncate(jwsPayloadString));
        System.out.println("[VERIFY] jws.payload (hex prefix): " + hexPrefix(jwsPayloadBytes));

        System.out.println("[VERIFY] rawBodyBytes length: " + rawBodyBytes.length);
        System.out.println("[VERIFY] rawBody (ISO-8859-1 preview): " + maybeTruncate(new String(rawBodyBytes, StandardCharsets.ISO_8859_1)));
        System.out.println("[VERIFY] rawBody (UTF-8 preview): " + maybeTruncate(new String(rawBodyBytes, StandardCharsets.UTF_8)));
        System.out.println("[VERIFY] rawBody (hex prefix): " + hexPrefix(rawBodyBytes));

        // Prepare candidate payload byte arrays to try (common differences)
        List<byte[]> candidates = new ArrayList<>();
        List<String> candidateNames = new ArrayList<>();

        // 0 - raw bytes as received
        candidates.add(rawBodyBytes);
        candidateNames.add("rawBytes");

        // 1 - raw interpreted as UTF-8 (string -> bytes(utf8))
        String rawAsUtf8 = new String(rawBodyBytes, StandardCharsets.UTF_8);
        candidates.add(rawAsUtf8.getBytes(StandardCharsets.UTF_8));
        candidateNames.add("raw->UTF8->bytes");

        // 2 - URL decoded (application/x-www-form-urlencoded decoding)
        try {
            String urlDecoded = URLDecoder.decode(rawAsUtf8, StandardCharsets.UTF_8.name());
            candidates.add(urlDecoded.getBytes(StandardCharsets.UTF_8));
            candidateNames.add("URLDecoded(raw UTF-8)");
        } catch (IllegalArgumentException e) {
            System.out.println("[VERIFY] URLDecoder failed: " + e.getMessage());
        }

        // 3 - replace '+' with space (common for form encoding)
        String plusToSpace = rawAsUtf8.replace("+", " ");
        candidates.add(plusToSpace.getBytes(StandardCharsets.UTF_8));
        candidateNames.add("plus->space");

        // 4 - trimmed trailing newline variants
        byte[] trimmed = trimTrailingNewlines(rawBodyBytes);
        candidates.add(trimmed);
        candidateNames.add("trimTrailingNewlines");

        // 5 - add trailing CRLF (some signers include)
        byte[] crlfAdded = appendCRLFIfMissing(rawBodyBytes);
        candidates.add(crlfAdded);
        candidateNames.add("appendCRLFIfMissing");

        // deduplicate candidates by bytes content to avoid repeated work
        List<byte[]> uniqueCandidates = new ArrayList<>();
        List<String> uniqueNames = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            byte[] c = candidates.get(i);
            boolean seen = false;
            for (byte[] u : uniqueCandidates) {
                if (Arrays.equals(u, c)) { seen = true; break; }
            }
            if (!seen) { uniqueCandidates.add(c); uniqueNames.add(candidateNames.get(i)); }
        }

        // Try verification for each candidate
        for (int i = 0; i < uniqueCandidates.size(); i++) {
            byte[] cand = uniqueCandidates.get(i);
            String name = uniqueNames.get(i);
            String payloadB64Candidate = Base64.getUrlEncoder().withoutPadding().encodeToString(cand);
            String signingInput = headerB64 + "." + payloadB64Candidate;

            System.out.println("[VERIFY TRY] candidate=" + name
                + " len=" + cand.length
                + " payloadB64Candidate(prefix)=" + (payloadB64Candidate.length() > 80 ? payloadB64Candidate.substring(0, 80) + "..." : payloadB64Candidate)
                + " signingInputLen=" + signingInput.length());

            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(signingCert.getPublicKey());
            verifier.update(signingInput.getBytes(StandardCharsets.US_ASCII));
            boolean valid;
            try {
                valid = verifier.verify(sig);
            } catch (SignatureException se) {
                valid = false;
            }
            System.out.println("[VERIFY TRY RESULT] candidate=" + name + " -> " + valid);
            if (valid) {
                System.out.println("[VERIFY] MATCH FOUND with candidate: " + name);
                return true;
            }
        }

        System.out.println("[VERIFY] no candidate matched -> signature invalid");
        return false;
    }

    private static byte[] trimTrailingNewlines(byte[] in) {
        int end = in.length;
        while (end > 0 && (in[end - 1] == '\n' || in[end - 1] == '\r')) end--;
        return Arrays.copyOf(in, end);
    }

    private static byte[] appendCRLFIfMissing(byte[] in) {
        if (in.length >= 2 && in[in.length - 2] == '\r' && in[in.length - 1] == '\n') {
            return in;
        }
        byte[] out = Arrays.copyOf(in, in.length + 2);
        out[out.length - 2] = '\r';
        out[out.length - 1] = '\n';
        return out;
    }
}
