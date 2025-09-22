package pl.oliwawyplywa.web.services.tpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Service
public class TpayCertService {

    private static final Logger logger = LoggerFactory.getLogger(TpayCertService.class);

    private final X509Certificate rootCert;
    private final X509Certificate signingCert;

    public TpayCertService(
        @Value("${payments.tpay.certs.jws-root}") String rootPath,
        @Value("${payments.tpay.certs.jws-notifications}") String signingPath) throws Exception {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // Загружаем корневой сертификат
        X509Certificate rootTemp;
        try (InputStream rootIs = new FileInputStream(rootPath)) {
            rootTemp = (X509Certificate) cf.generateCertificate(rootIs);
            logger.info("Root certificate loaded from '{}', subject: {}", rootPath, rootTemp.getSubjectX500Principal());
        } catch (Exception e) {
            logger.error("Failed to load root certificate from '{}'", rootPath, e);
            throw e;
        }
        this.rootCert = rootTemp;

        // Загружаем сертификат подписи
        X509Certificate signingTemp;
        try (InputStream signIs = new FileInputStream(signingPath)) {
            signingTemp = (X509Certificate) cf.generateCertificate(signIs);
            logger.info("Signing certificate loaded from '{}', subject: {}", signingPath, signingTemp.getSubjectX500Principal());
        } catch (Exception e) {
            logger.error("Failed to load signing certificate from '{}'", signingPath, e);
            throw e;
        }
        this.signingCert = signingTemp;

        // Проверяем цепочку сразу при старте
        try {
            signingCert.verify(rootCert.getPublicKey());
            logger.info("Certificate chain verification succeeded");
        } catch (Exception e) {
            logger.error("Certificate chain verification failed", e);
            throw new Exception("Certificate chain validation failed", e);
        }
    }

    public X509Certificate getSigningCert() {
        return signingCert;
    }

    public X509Certificate getRootCert() {
        return rootCert;
    }
}
