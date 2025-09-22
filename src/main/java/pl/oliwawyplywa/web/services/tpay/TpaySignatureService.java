package pl.oliwawyplywa.web.services.tpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TpaySignatureService {

    private static final Logger logger = LoggerFactory.getLogger(TpaySignatureService.class);

    private final TpayCertService certService;

    private static final String EXPECTED_X5U = "https://secure.tpay.com/x509/notifications-jws.pem";
    private static final Pattern X5U_PATTERN = Pattern.compile("\"x5u\":\"([^\"]+)\"");

    public TpaySignatureService(TpayCertService certService) {
        this.certService = certService;
    }

    public Mono<Boolean> verifySignature(String jws, String body) {
        String[] parts = jws.split("\\.");
        if (parts.length != 3) {
            logger.warn("Invalid JWS format: wrong number of parts");
            return Mono.just(false);
        }

        String headerB64 = parts[0];
        String sigB64 = parts[2];

        // Decode header
        byte[] headerBytes = Base64.getUrlDecoder().decode(headerB64);
        String headerJson = new String(headerBytes, StandardCharsets.UTF_8);

        // Extract x5u using regex
        Matcher matcher = X5U_PATTERN.matcher(headerJson);
        String x5u = null;
        if (matcher.find()) {
            x5u = matcher.group(1);
        }

        if (x5u == null) {
            logger.warn("Missing x5u in JWS header");
            return Mono.just(false);
        }

        if (!EXPECTED_X5U.equals(x5u)) {
            logger.warn("Invalid x5u URL: expected={}, received={}", EXPECTED_X5U, x5u);
            return Mono.just(false);
        }

        // Encode payload (raw body) to base64url without padding
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(bodyBytes);

        // Decode signature
        byte[] decodedSig = Base64.getUrlDecoder().decode(sigB64);

        // Use pre-loaded signing certificate
        try {
            X509Certificate signingCert = certService.getSigningCert();

            // Verify JWS signature
            java.security.PublicKey publicKey = signingCert.getPublicKey();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update((headerB64 + "." + payloadB64).getBytes(StandardCharsets.UTF_8));
            boolean isValid = signature.verify(decodedSig);

            if (isValid) {
                logger.debug("JWS signature verified successfully");
            } else {
                logger.warn("JWS signature verification failed");
            }
            return Mono.just(isValid);
        } catch (Exception e) {
            logger.error("Error during JWS verification", e);
            return Mono.just(false);
        }
    }

}
