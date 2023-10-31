package com.project.animal.Test;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

// https://min.io/docs/minio/linux/developers/java/API.html 도큐먼트 문서
@Component
@RequiredArgsConstructor
public class MinioTest {

    private final MinioClient minioClient;

//    @EventListener(ApplicationReadyEvent.class)
    public void test() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        // 1. 버킷 유무 판단
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("akgkfk3").build());

        if (found) {
            System.out.println("존재");
        } else {
            System.out.println("미존재");
        }

        // 2. 버킷 내 저장된 Item (파일) 목록 조회
        Iterable<Result<Item>> itemList = minioClient.listObjects(ListObjectsArgs.builder().bucket("test").build());

        Iterator<Result<Item>> it = itemList.iterator();

        while (it.hasNext()) {
            Result<Item> next = it.next();
            System.out.println(next.get().size());
        }

        // 3. 파일 업로드 (텍스트 파일)
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("test")
                        .object("test.txt")
                        .filename("C:\\Users\\sHu\\Desktop\\mockInvestment\\src\\main\\java\\sesac\\mockInvestment\\test.txt")
                        .build()
        );

        // 4. 파일 업로드 (바이너리)
        InputStream is = new FileInputStream("C:\\Users\\sHu\\Desktop\\mockInvestment\\src\\main\\java\\sesac\\mockInvestment\\test.txt");
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("test")
                        .object("test2.txt")
                        .stream(is, is.available(), 0)
                        .build()
        );

        // 5. 파일 다운로드 (텍스트)
        minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket("test")
                        .object("test2.txt")
                        .filename("C:\\Users\\sHu\\Desktop\\mockInvestment\\src\\main\\java\\sesac\\mockInvestment\\test3.txt")
                        .build()
        );

        // 6. 파일 다운로드 (바이너리)
        InputStream response = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("test")
                        .object("test.txt")
                        .build()
        );
        System.out.println(StreamUtils.copyToString(response, StandardCharsets.UTF_8));

        response.close();

        // 7. 파일 삭제
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket("test")
                        .object("test2.txt")
                        .build()
        );
    }
}