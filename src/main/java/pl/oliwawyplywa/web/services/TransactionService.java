package pl.oliwawyplywa.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public Mono<Void> processTransaction(MultiValueMap<String, String> params) {
        String trId = params.getFirst("tr_id");
        String trCrc = params.getFirst("tr_crc"); // Use for idempotency, e.g., order ID

        // Check if already processed (idempotency)
        // if (transactionRepository.existsByTrId(trId)) {
        //     logger.info("Transaction {} already processed, skipping", trId);
        //     return Mono.empty();
        // }

        // Process the payment: update order status, etc.
        logger.info("Processing transaction: tr_id={}, tr_crc={}, amount={}", trId, trCrc, params.getFirst("tr_amount"));

        // Save as processed
        // transactionRepository.save(new TransactionEntity(trId, ...));

        return Mono.empty(); // Or actual async operation
    }

}
