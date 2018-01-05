package com.prototype.server.prototypeserver.config;

import org.apache.commons.mail.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MailConfig {





    private MailConfig() {

    }

    public static void send(String name, String mail, String subject, String body) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("p347255.mail.ihc.ru");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("income@cpay.click", "a123456A"));
        email.setSSLOnConnect(true);
        email.setFrom("income@cpay.click", "CPay.store");


        // set the html message
        email.setSubject(subject);
        email.setHtmlMsg(body);

        email.setCharset(StandardCharsets.UTF_8.name());
        email.addTo(mail);


        email.send();
    }
}
