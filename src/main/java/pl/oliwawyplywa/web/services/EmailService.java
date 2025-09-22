package pl.oliwawyplywa.web.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import pl.oliwawyplywa.web.repositories.OrderItemsRepository;
import pl.oliwawyplywa.web.schemas.Order;
import pl.oliwawyplywa.web.schemas.OrderItem;
import reactor.core.publisher.Mono;

import org.thymeleaf.context.Context;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmailService {

    private final OrderItemsRepository orderItemsRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final OrderCalculationService orderCalculationService;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, OrderCalculationService orderCalculationService, OrderItemsRepository orderItemsRepository) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.orderCalculationService = orderCalculationService;
        this.orderItemsRepository = orderItemsRepository;
    }

    public Mono<Void> sendOrderEmails(Order order) {
        Mono<List<OrderItem>> orderItemsMono = orderItemsRepository.getOrderItemsWithOptionsByOrderId(order.getOrderId())
            .collectList()
            .switchIfEmpty(Mono.just(List.of()));

        Mono<BigDecimal> totalMono = orderCalculationService.calculateOrderTotal(order.getOrderId());

        return Mono.zip(orderItemsMono, totalMono)
            .flatMap(tuple -> {
                Context userContext = getContext(order, tuple);
                String userHtml = templateEngine.process("user-confirmation", userContext);

                return Mono.fromRunnable(() -> sendEmail(
                    order.getEmail(),
                    "Dziękujemy za zamówienie!",
                    userHtml
                )).subscribeOn(Schedulers.boundedElastic()).then();
            })
            .onErrorResume(e -> {
                System.err.println("Failed to send order emails: " + e.getMessage());
                return Mono.error(new RuntimeException("Failed to send order emails", e));
            });
    }

    private static Context getContext(Order order, Tuple2<List<OrderItem>, BigDecimal> tuple) {
        List<OrderItem> orderItems = tuple.getT1();
        BigDecimal calculatedTotal = tuple.getT2();

        Context userContext = new Context();
        userContext.setVariable("userFullName", order.getFullName());
        userContext.setVariable("userAddress", order.getAddress());
        //        userContext.setVariable("userPhone", user.getPhone());
        userContext.setVariable("products", orderItems);
        userContext.setVariable("total", calculatedTotal);

        return userContext;
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
