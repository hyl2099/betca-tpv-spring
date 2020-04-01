package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class UserReactRepositoryIT {

    @Autowired
    private UserReactRepository userReactRepository;

    @Test
    void testFindAllAndDatabaseSeeder() {
        StepVerifier
                .create(this.userReactRepository.findAll())
                .expectNextCount(1)
                .expectNextMatches(user -> {
                    assertEquals("666666000", user.getMobile());
                    assertEquals("all-roles", user.getUsername());
                    assertNotEquals("p000", user.getPassword());
                    assertEquals("C/TPV, 0, MIW", user.getAddress());
                    assertEquals("u000@gmail.com", user.getEmail());
                    assertNull(user.getDni());
                    assertNotNull(user.getRegistrationDate());
                    assertTrue(user.isActive());
                    assertNotNull(user.getRoles());
                    assertFalse(user.toString().matches("@"));
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindAllAndDatabaseSeederCheckingMessages() {
        List<Messages> messagesList = new ManagedList<>();
        LocalDateTime sentTime = LocalDateTime.of(2020, 3, 13, 9, 0, 0);
        LocalDateTime readTime = LocalDateTime.of(2020, 3, 14, 9, 0, 0);
        messagesList.add(new Messages("u002", "Message fromm u002 to u007", sentTime, readTime));
        messagesList.add(new Messages("u003", "Message fromm u003 to u007", sentTime.plusDays(1), readTime.plusDays(1)));
        StepVerifier
                .create(this.userReactRepository.findAll())
                .expectNextCount(8)
                .expectNextMatches(user -> {
                    assertEquals("666666007", user.getMobile());
                    assertEquals("u007", user.getUsername());
                    assertNotEquals("p007", user.getPassword());
                    assertEquals("66666607W", user.getDni());
                    assertEquals("C/TPV, 7", user.getAddress());
                    assertEquals("u007@gmail.com", user.getEmail());
                    assertNotNull(user.getRegistrationDate());
                    assertTrue(user.isActive());
                    assertNotNull(user.getRoles());
                    assertFalse(user.toString().matches("@"));
                    assertEquals(messagesList.toString(), user.getMessagesList().toString());
                    return true;
                })
                .thenCancel()
                .verify();
    }

}

