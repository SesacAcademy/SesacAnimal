package com.project.animal.Test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class restTemplate {

    public static void main(String[] args) {
        // https://velog.io/@soosungp33/%EC%8A%A4%ED%94%84%EB%A7%81-RestTemplate-%EC%A0%95%EB%A6%AC%EC%9A%94%EC%B2%AD-%ED%95%A8

        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "https://api.upbit.com/v1/ticker?markets=KRW-BTC"; // 원하는 API의 엔드포인트 URL

        // GET 요청을 보내고 응답을 ResponseEntity로 받음
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

        // 응답 데이터 추출
        String responseData = responseEntity.getBody();

        System.out.println(responseData);

        // responseData를 가지고 원하는 작업을 수행
        // 예를 들어, JSON 데이터를 파싱하거나 처리하는 등의 작업을 수행할 수 있음
    }
}
