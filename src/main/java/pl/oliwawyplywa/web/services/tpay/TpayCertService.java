package pl.oliwawyplywa.web.services.tpay;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Service
public class TpayCertService {

    @Value("${payments.tpay.certs.jws-root}")
    private String rootPathString;

    private X509Certificate rootCert;

    @PostConstruct
    public void init() throws Exception {
        Path rootPath = Path.of(rootPathString);
        byte[] rootBytes = Files.readAllBytes(rootPath);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        this.rootCert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(rootBytes));
    }

    public void verifyCertificateChain(X509Certificate signingCert) throws Exception {
        PublicKey rootKey = rootCert.getPublicKey();
        signingCert.verify(rootKey);
    }
}
