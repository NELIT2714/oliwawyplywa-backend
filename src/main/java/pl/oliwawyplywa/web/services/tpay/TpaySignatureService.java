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

    public boolean verify(String jws, String rawBody) throws Exception {
        if (jws == null || jws.isEmpty()) return false;

        String[] parts = jws.split("\\.");
        if (parts.length != 3) return false;

        String headerB64 = parts[0];
        String signatureB64 = parts[2];

        // decode header JSON
        String headerJson = new String(Base64.getUrlDecoder().decode(headerB64), StandardCharsets.UTF_8);
        Map<String, Object> header = new ObjectMapper().readValue(headerJson, Map.class);

        if (!header.containsKey("x5u")) return false;
        String x5u = header.get("x5u").toString();
        if (!x5u.startsWith("https://secure.tpay.com")) return false;

        // load signing cert
        byte[] certBytes = URI.create(x5u).toURL().openStream().readAllBytes();
        X509Certificate signingCert = (X509Certificate) CertificateFactory.getInstance("X.509")
            .generateCertificate(new ByteArrayInputStream(certBytes));

        // validate chain
        certService.verifyCertificateChain(signingCert);

        // body już jest form-urlencoded → bierzemy wprost
        String payloadB64 = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(rawBody.getBytes(StandardCharsets.UTF_8));

        String signingInput = headerB64 + "." + payloadB64;

        byte[] sig = Base64.getUrlDecoder().decode(signatureB64);

        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(signingCert.getPublicKey());
        verifier.update(signingInput.getBytes(StandardCharsets.UTF_8));

        return verifier.verify(sig);
    }
}