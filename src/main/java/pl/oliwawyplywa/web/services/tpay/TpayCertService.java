package pl.oliwawyplywa.web.services.tpay;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.*;
import java.util.List;
import java.util.Set;

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
        System.out.println("Tpay root certificate loaded: " + rootCert.getSubjectDN());
    }

    public void verifyCertificateChain(X509Certificate signingCert) throws Exception {
        // просто проверяем подпись signingCert через rootCert
        signingCert.verify(rootCert.getPublicKey());
        System.out.println("Signing certificate verified against root");
    }
}