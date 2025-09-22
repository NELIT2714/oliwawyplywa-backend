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
    }

    public void verifyCertificateChain(X509Certificate signingCert) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        CertPath certPath = cf.generateCertPath(List.of(signingCert));

        TrustAnchor anchor = new TrustAnchor(rootCert, null);
        PKIXParameters params = new PKIXParameters(Set.of(anchor));
        params.setRevocationEnabled(false);

        CertPathValidator validator = CertPathValidator.getInstance("PKIX");
        validator.validate(certPath, params);
    }
}
