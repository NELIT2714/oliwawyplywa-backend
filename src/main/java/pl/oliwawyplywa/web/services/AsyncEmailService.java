package pl.oliwawyplywa.web.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

//@Service
public class AsyncEmailService {

    private final JavaMailSender mailSender;

    public AsyncEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Mono<Void> sendEmailAsync(String to, String subject, String text) {
        return Mono.fromCallable(() -> {
                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);
                // Опционально: message.setFrom("your-email@gmail.com"); // Если не указано, берется из config
                mailSender.send(message);

                return null;
            })
            .subscribeOn(Schedulers.boundedElastic())
            .then();
    }

    public Mono<Void> sendHtmlEmailAsync(String to, String subject, String htmlContent) {
        return Mono.fromCallable(() -> {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);

                mailSender.send(mimeMessage);

                return null;
            })
            .subscribeOn(Schedulers.boundedElastic())
            .then();
    }
}
