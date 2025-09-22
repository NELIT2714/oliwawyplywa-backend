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
                                       @RequestBody Mono<String> bodyMono) {
        return bodyMono.flatMap(body -> {
            System.out.println("RAW BODY: " + body);
            try {
                boolean ok = signatureService.verify(jws, body);
                return Mono.just(ok ? "TRUE" : "FALSE - invalid signature");
            } catch (Exception e) {
                return Mono.just("FALSE - exception: " + e.getMessage());
            }
        });
    }
}