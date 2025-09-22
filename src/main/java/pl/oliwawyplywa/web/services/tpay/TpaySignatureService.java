package pl.oliwawyplywa.web.services.tpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TpaySignatureService {

    private final TpayCertService certService;

    public TpaySignatureService(TpayCertService certService) {
        this.certService = certService;
    }

    public boolean verify(String jws, byte[] rawBodyBytes) throws Exception {
        if (jws == null || jws.isEmpty()) return false;

        String[] parts = jws.split("\\.");
        if (parts.length != 3) return false;

        String headerB64 = parts[0];
        String signatureB64 = parts[2];

        // Декодируем header
        String headerJson = new String(Base64.getUrlDecoder().decode(headerB64), StandardCharsets.UTF_8);
        Map<String, Object> header = new ObjectMapper().readValue(headerJson, Map.class);

        if (!header.containsKey("x5u")) return false;

        // Берем локальный сертификат подписи
        X509Certificate signingCert = certService.getSigningCert();

        // --- Canonical form payload ---
        String bodyStr = new String(rawBodyBytes, StandardCharsets.ISO_8859_1); // Tpay присылает в ISO-8859-1
        Map<String, String> params = Arrays.stream(bodyStr.split("&"))
            .map(s -> s.split("=", 2))
            .collect(Collectors.toMap(
                a -> a[0],
                a -> a.length > 1 ? URLDecoder.decode(a[1], StandardCharsets.UTF_8) : ""
            ));

        // Сортируем параметры по ключам (строго)
        String canonicalPayload = params.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"));

        // Base64url
        String payloadB64 = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(canonicalPayload.getBytes(StandardCharsets.UTF_8));

        byte[] sig = Base64.getUrlDecoder().decode(signatureB64);

        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(signingCert.getPublicKey());
        verifier.update((headerB64 + "." + payloadB64).getBytes(StandardCharsets.US_ASCII));

        boolean valid = verifier.verify(sig);

        if (!valid) {
            System.out.println("[TPAY ERROR] Invalid JWS signature for payload: " + canonicalPayload);
        }

        return valid;
    }
}
