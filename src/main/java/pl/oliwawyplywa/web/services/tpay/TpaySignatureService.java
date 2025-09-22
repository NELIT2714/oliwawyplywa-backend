package pl.oliwawyplywa.web.services.tpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
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


    public boolean verifyWithNimbus(String jws, byte[] rawBodyBytes) throws Exception {
        if (jws == null || jws.isEmpty()) return false;

        String[] parts = jws.split("\\.");
        if (parts.length != 3) return false;

        String headerB64 = parts[0];
        String signatureB64 = parts[2];

        X509Certificate signingCert = certService.getSigningCert();
        RSAPublicKey rsaPubKey = (RSAPublicKey) signingCert.getPublicKey();

        // Payload как Base64URL из raw body
        Base64URL payloadB64 = Base64URL.encode(rawBodyBytes);

        // Создаём JWSObject с payload
        JWSObject jwsObject = new JWSObject(
            new Base64URL(headerB64),
            new Payload(payloadB64),
            new Base64URL(signatureB64)
        );

        RSASSAVerifier verifier = new RSASSAVerifier(rsaPubKey);

        return jwsObject.verify(verifier);
    }

}
