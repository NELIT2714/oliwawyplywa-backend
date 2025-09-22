package pl.oliwawyplywa.web.controllers;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
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

                return Mono.fromCallable(() -> signatureService.verify(jws, bodyBytes))
                    .map(ok -> ok ? "TRUE" : "FALSE - invalid signature")
                    .onErrorReturn("FALSE - exception during verification");
            });
    }
}