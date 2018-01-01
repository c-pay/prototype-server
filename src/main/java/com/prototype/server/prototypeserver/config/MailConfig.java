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
        email.setHostName("smtp.yandex.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("hashfactory@yandex.ru", "1234Zz"));
        email.setSSLOnConnect(true);
        email.setFrom("hashfactory@yandex.ru", "HashFactory.ru");


        // set the html message
        email.setSubject(subject);
        email.setHtmlMsg(body);

        email.setCharset(StandardCharsets.UTF_8.name());
        email.addTo(mail);


        email.send();
    }
}
