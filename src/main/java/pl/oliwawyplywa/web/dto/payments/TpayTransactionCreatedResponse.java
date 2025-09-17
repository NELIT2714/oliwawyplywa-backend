package pl.oliwawyplywa.web.dto.payments;

import java.math.BigDecimal;

public class TpayTransactionCreatedResponse {

    private String result;
    private String requestId;
    private String transactionId;
    private String title;
    private String posId;
    private Object date;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String hiddenDescription;
    private String transactionPaymentUrl;

    public String getResult() {
        return result;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTitle() {
        return title;
    }

    public String getPosId() {
        return posId;
    }

    public Object getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getHiddenDescription() {
        return hiddenDescription;
    }

    public String getTransactionPaymentUrl() {
        return transactionPaymentUrl;
    }
}
