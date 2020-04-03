package es.upm.miw.betca_tpv_spring.business_services;

import es.upm.miw.betca_tpv_spring.exceptions.MailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
class MailService {

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private MailSender mailSender;

    void sendMail(String to, String subject, String message) {
        try {
            if (to.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(this.username);
                simpleMailMessage.setTo(to);
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(message);
                this.mailSender.send(simpleMailMessage);
            } else {
                throw new Exception("Recipient email address is not valid");
            }
        } catch (Exception ex) {
            throw new MailException("An error has occurred while sending the mail. " + ex.getMessage());
        }
    }
}
