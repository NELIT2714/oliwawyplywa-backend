package pl.oliwawyplywa.web.services.tpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.oliwawyplywa.web.dto.payments.BearerToken;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    private final AtomicReference<String> token = new AtomicReference<>();
    private final AtomicReference<Instant> expiresAt = new AtomicReference<>();

    private final Sinks.One<String> refreshSink = Sinks.one();

    private final TpayConfig config;

    public TokenManager(TpayConfig config) {
        this.config = config;
    }

    public Mono<String> getToken() {
        if (isExpired()) return refreshToken();
        return Mono.just(token.get());
    }

    private boolean isExpired() {
        Instant exp = expiresAt.get();
        return exp == null || Instant.now().isAfter(exp.minusSeconds(120));
    }

    private Mono<String> refreshToken() {
        if (refreshSink.currentSubscriberCount() != 0) return refreshSink.asMono();
        Sinks.One<String> newSink = Sinks.one();

        return callAuthApi()
            .map(resp -> {
                token.set(resp.getAccessToken());
                Instant issued = Instant.ofEpochSecond(resp.getIssuedAt());
                Instant expiry = issued.plusSeconds(resp.getExpiresIn());
                expiresAt.set(expiry);
                return resp.getAccessToken();
            })
            .doOnNext(newSink::tryEmitValue)
            .doOnError(newSink::tryEmitError)
            .then(newSink.asMono());
    }

    private Mono<BearerToken> callAuthApi() {
        logger.info("Tpay: Calling auth api");
        return WebClient.create(config.getApiUrl())
            .post()
            .uri("/oauth/auth")
            .bodyValue(Map.of(
                "client_id", config.getClientId(),
                "client_secret", config.getClientSecret()
            ))
            .retrieve()
            .bodyToMono(BearerToken.class);
    }

}
