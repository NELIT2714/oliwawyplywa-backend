package pl.oliwawyplywa.web.controllers;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oliwawyplywa.web.services.tpay.TpaySignatureService;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/tpay")
public class TpayCallbackController {

    private final TpaySignatureService signatureService;

    public TpayCallbackController(TpaySignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @PostMapping(value = "/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<String> handleCallback(@RequestHeader("X-JWS-Signature") String jws,
                                       ServerHttpRequest request) {

        System.out.println("[CALLBACK] X-JWS-Signature: " + (jws == null ? "null" : jws));

        return DataBufferUtils.join(request.getBody())
            .flatMap(dataBuffer -> {
                byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bodyBytes);
                DataBufferUtils.release(dataBuffer);

                System.out.println("[CALLBACK BODY (raw ISO-8859-1)] " + new String(bodyBytes, StandardCharsets.ISO_8859_1));
                System.out.println("[CALLBACK BODY (raw UTF-8 preview)] " +
                    new String(bodyBytes, StandardCharsets.UTF_8)
                        .replaceAll("\\s+", " ")
                        .substring(0, Math.min(200, new String(bodyBytes, StandardCharsets.UTF_8).length())));

                return Mono.fromCallable(() -> {
                        try {
                            boolean valid = signatureService.verify(jws, bodyBytes);
                            System.out.println("[TPAY] Signature valid? " + valid);
                            return valid;
                        } catch (Exception e) {
                            System.out.println("[TPAY ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
                            e.printStackTrace();
                            throw e;
                        }
                    })
                    .map(ok -> ok ? "TRUE" : "FALSE - invalid signature")
                    .onErrorReturn("FALSE - exception during verification");
            });
    }
}
