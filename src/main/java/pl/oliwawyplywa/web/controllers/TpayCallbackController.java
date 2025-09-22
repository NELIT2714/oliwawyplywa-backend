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
        System.out.println(jws);
        System.out.println(request);
        System.out.println(request.getBody());
        return DataBufferUtils.join(request.getBody())
            .map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                return new String(bytes, StandardCharsets.UTF_8);
            })
            .flatMap(body -> {
                try {
                    boolean ok = signatureService.verify(jws, body);
                    System.out.println("Verification result: " + ok);
                    return Mono.just(ok ? "TRUE" : "FALSE - invalid signature");
                } catch (Exception e) {
                    System.out.println("Verification exception: " + e.getMessage());
                    return Mono.just("FALSE - exception: " + e.getMessage());
                }
            });
    }
}