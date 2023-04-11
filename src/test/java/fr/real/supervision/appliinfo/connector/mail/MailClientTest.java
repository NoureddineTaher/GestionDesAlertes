package fr.real.supervision.appliinfo.connector.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MailClientTest {

    @Autowired
    private MailClient mailClient;

    private GreenMail smtpServer;

    @Before
    public void setUp() throws Exception {
        smtpServer = new GreenMail(new ServerSetup(25, null, "smtp"));
        smtpServer.start();
    }

    @Ignore("Problème de build")
    @Test
    public void shouldSendMail() throws Exception {
        //given
        //String recipient = "laurent.magnien.groupeadsn@notaires.fr";
        String[] recipients = {"didier.vanderstoken@intranet-adsn.fr"};
        String message = "Test message content";
        String from = "dev.notaire@intranet-adsn.fr";
        String subject = "test";

        //when
        mailClient.prepareAndSend(from, recipients, subject, message);
        //then
        String content = "<span>" + message + "</span>";
        assertReceivedMessageContains(content);
    }

    private void assertReceivedMessageContains(String expected) throws IOException, MessagingException {
        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        String content = (String) receivedMessages[0].getContent();
        assertTrue(content.contains(expected));
    }

    @After
    public void tearDown() throws Exception {
        smtpServer.stop();
    }

}