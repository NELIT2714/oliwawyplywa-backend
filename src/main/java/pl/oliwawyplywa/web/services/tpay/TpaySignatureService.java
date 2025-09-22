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

    public boolean verify(String jws) {
        try {
            if (jws == null || jws.isEmpty()) return false;

            // Парсим JWS
            JWSObject jwsObject = JWSObject.parse(jws);

            // Берем локальный сертификат Tpay
            X509Certificate signingCert = certService.getSigningCert();

            // Создаем верификатор для RSA
            RSASSAVerifier verifier = new RSASSAVerifier(signingCert.getPublicKey());

            // Проверяем подпись
            boolean valid = jwsObject.verify(verifier);

            if (!valid) {
                System.out.println("[TPAY ERROR] Invalid JWS signature");
                System.out.println("Payload: " + jwsObject.getPayload().toString());
            }

            return valid;

        } catch (Exception e) {
            System.out.println("[TPAY ERROR] Exception during signature verification: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
