package pl.oliwawyplywa.web.services.tpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
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

        X509Certificate signingCert = certService.getSigningCert();
        RSAPublicKey rsaPubKey = (RSAPublicKey) signingCert.getPublicKey();

        // Собираем полный JWS: header..payload..signature
        String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(rawBodyBytes);
        String fullJws = jws.split("\\.")[0] + "." + payloadB64 + "." + jws.split("\\.")[2];

        JWSObject jwsObject = JWSObject.parse(fullJws);
        RSASSAVerifier verifier = new RSASSAVerifier(rsaPubKey);

        return jwsObject.verify(verifier);
    }

}
