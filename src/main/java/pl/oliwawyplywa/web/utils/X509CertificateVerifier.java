package pl.oliwawyplywa.web.utils;

import reactor.core.publisher.Mono;

import java.security.cert.X509Certificate;

public class X509CertificateVerifier {

    public Mono<Boolean> verifyCertificate(X509Certificate signingCert, X509Certificate caCert) {
        return Mono.fromCallable(() -> {
            try {
                signingCert.verify(caCert.getPublicKey());
                return true;
            } catch (Exception e) {
                return false;
            }
        }).onErrorReturn(false);
    }
}
