package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.models.NotificationEmail;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
class MailService {

    private final JavaMailSender mailSender;

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("fakeddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e
            );
            throw new CustomException(
                    "Exception occurred when sending mail to " + notificationEmail.getRecipient()
            );
        }
    }

}