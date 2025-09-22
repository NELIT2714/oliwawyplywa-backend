package pl.oliwawyplywa.web.services.tpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.regex.Pattern;

@Service
public class TpaySignatureService {

    private static final Logger logger = LoggerFactory.getLogger(TpaySignatureService.class);

    private final TpayCertService certService;

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

        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(bodyBytes);

        byte[] decodedSig = Base64.getUrlDecoder().decode(sigB64);

        try {
            X509Certificate signingCert = certService.getSigningCert();

            PublicKey publicKey = signingCert.getPublicKey();
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
