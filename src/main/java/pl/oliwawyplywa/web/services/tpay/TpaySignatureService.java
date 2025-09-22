package pl.oliwawyplywa.web.services.tpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

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

        // --- Важно: payload кодируем строго как Base64url из исходного тела ---
        String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(rawBodyBytes);

        byte[] sig = Base64.getUrlDecoder().decode(signatureB64);

        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(signingCert.getPublicKey());
        verifier.update((headerB64 + "." + payloadB64).getBytes(StandardCharsets.US_ASCII));

        boolean valid = verifier.verify(sig);

        if (!valid) {
            System.out.println("[TPAY ERROR] Invalid JWS signature for payload: " + new String(rawBodyBytes, StandardCharsets.UTF_8));
        }

        return valid;
    }
}
