package pl.oliwawyplywa.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oliwawyplywa.web.services.tpay.TpayPaymentService;
import pl.oliwawyplywa.web.services.tpay.TpaySignatureService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/tpay")
public class TpayCallbackController {

    private final TpayPaymentService tpayPaymentService;

    public TpayCallbackController(TpayPaymentService tpayPaymentService) {
        this.tpayPaymentService = tpayPaymentService;
    }

    @PostMapping(value = "/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<String> handleCallback(@RequestHeader("X-JWS-Signature") String jws,
                                       ServerHttpRequest request) {
        return tpayPaymentService.handleCallback(jws, request);
    }

}
