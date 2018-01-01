package com.prototype.server.prototypeserver.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5MB");
        factory.setMaxRequestSize("5MB");
        return factory.createMultipartConfig();
    }

//    @Bean
//    public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
//        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
//            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
//                //-1 for unlimited
//                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
//            }
//        });
//        return tomcat;
//    }
}