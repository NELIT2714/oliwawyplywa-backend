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

        return DataBufferUtils.join(request.getBody())
            .map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return new String(bytes, StandardCharsets.UTF_8);
            })
            .flatMap(body -> {
                // логируем body
                System.out.println("[CALLBACK BODY] " + body);
                return Mono.fromCallable(() -> signatureService.verify(jws, body))
                    .map(ok -> ok ? "TRUE" : "FALSE - invalid signature")
                    .doOnError(e -> System.out.println("[CALLBACK ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage()))
                    .onErrorReturn("FALSE - exception during verification");
            });
    }
}