package pl.oliwawyplywa.web.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TpayConfig {

    private static final Logger logger = LoggerFactory.getLogger(TpayConfig.class);

    @Value("${payments.tpay.api_url}")
    private String apiUrl;

    @Value("${payments.tpay.client_id}")
    private String clientId;

    @Value("${payments.tpay.client_secret}")
    private String clientSecret;

    @PostConstruct
    public void validate() {
        if (apiUrl == null || clientId == null || clientSecret == null) {
            logger.warn("Tpay: API url, Client ID or Client Secret is null");
        }
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
