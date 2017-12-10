package com.prototype.server.prototypeserver;

import org.springframework.web.client.RestTemplate;

public class TestRest {
    public static void main(String[] args) {
        System.out.println("TEST");
        RestTemplate restTemplate = new RestTemplate();
//        ListAdvertDTO objects = restTemplate.getForObject("http://192.168.0.101:8080/", ListAdvertDTO.class);
//        System.out.println(objects);

    }
}
