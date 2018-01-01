package com.prototype.server.prototypeserver.service;

import com.prototype.server.prototypeserver.config.MailConfig;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    public void send(String name, String mail, String subject, String body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MailConfig.send(name, mail, subject, body);
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
