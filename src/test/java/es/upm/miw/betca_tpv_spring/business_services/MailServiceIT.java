package es.upm.miw.betca_tpv_spring.business_services;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.exceptions.ConflictException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@TestConfig
class MailServiceIT {

    @MockBean
    private MailSender mailSender;

    @Autowired
    private MailService mailService;

    @Value("${spring.mail.username}")
    private String fromUsername;

    @Value("${spring.mail.valid.username}")
    private String validUsername;

    @Value("${spring.mail.invalid.username}")
    private String notValidUsername;

    @Test
    void testSendEmail() {
        final ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        final String SUBJECT = "test number 1";
        final String MESSAGE = "body message number 1";
        this.mailService.sendMail(this.validUsername, SUBJECT, MESSAGE);
        verify(this.mailSender).send(captor.capture());
        assertEquals(this.fromUsername, captor.getValue().getFrom());
        assertEquals(this.validUsername, Objects.requireNonNull(captor.getValue().getTo())[0]);
        assertEquals(SUBJECT, captor.getValue().getSubject());
        assertEquals(MESSAGE, captor.getValue().getText());
    }

    @Test
    void testSendEmailException() {
        final String SUBJECT = "test number 2";
        final String MESSAGE = "body message number 2";
        assertThrows(ConflictException.class,
                () -> this.mailService.sendMail(this.notValidUsername, SUBJECT, MESSAGE));
    }
}
