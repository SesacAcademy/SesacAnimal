package com.project.animal.adoption.test;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class restTemplate {

    public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException {
        // https://velog.io/@soosungp33/%EC%8A%A4%ED%94%84%EB%A7%81-RestTemplate-%EC%A0%95%EB%A6%AC%EC%9A%94%EC%B2%AD-%ED%95%A8

        RestTemplate restTemplate = new RestTemplate();

        String fullUrl = "http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=EzxLIzlCVgYLjZw0%2F80cOsMioDse3rHqkMt05%2FHotyGORQ3xcT6%2BFyqR2o%2B8XXuNh37OnCNoCGE7twsnT2N%2Bkg%3D%3D&pageNo=1&numOfRows=100&_type=json";

        URI uri = new URI(fullUrl);
        String result = restTemplate.getForObject(uri, String.class);
//        String result1 = restTemplate.getForEntity(uri, Entity.class);

        System.out.println("res: >> " + result);

    }

}


