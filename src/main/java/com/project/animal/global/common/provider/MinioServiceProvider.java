package com.project.animal.global.common.provider;

import com.project.animal.global.common.dto.ImageListDto;
import com.project.animal.global.common.providrerror.minioException;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Log4j2
@RequiredArgsConstructor
@Component
public class MinioServiceProvider {
    @Autowired
    private MinioClient minioClient;
    public final String REVIEW = "review";
    public final String ADOPTION = "adoption";
    public final String MISSING = "missing";

    private String getImgUrl(MultipartFile file){
        return  "/" +  UUID.randomUUID() + "_" +file.getName() +"." +getExtension(file);
    }
    private boolean checkBucket(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            return found;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public List<String> insertImageMinio(ImageListDto imageListDto, String type) {
        List<String> urls = new ArrayList<>();
        try {
            if (checkBucket(type)) {
                if (imageDtoSizeCheck(imageListDto)){
                    List<MultipartFile> images = imageListDto.getImageList();
                    for (MultipartFile image:images) {
                        if (!image.isEmpty()){
                            if (image.getSize()>=10*1024*1024){
                                throw new minioException(image.getName()+": 파일의 용량이 너무 큽니다");
                            }
                            String url = uploadImageToMinio(image, type);
                            urls.add(url);
                        }
                    }
                }
            }
        } catch (MinioException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return urls;
    }
    private boolean imageDtoSizeCheck(ImageListDto imageListDto) throws MinioException {
        int userInputCount = imageListDto.getImageCount();
        int inputImageCount = imageListDto.getImageList().size();
        if (inputImageCount==userInputCount){
            return true;
        }else {
            throw new MinioException("유저가 올리고자 하는 이미지가 제대로 등록되지 않았습니다." +
                    " 유저가 올리고자 하는 이미지: " + userInputCount + " 등록된 이미지 갯수: " + inputImageCount);
        }
    }
    private String getExtension(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }
    private String uploadImageToMinio(MultipartFile image, String type){
        try {
            String url = getImgUrl(image);
            InputStream is = image.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(type)
                            .object(url)
                            .stream(is, is.available(), 0)
                            .contentType(image.getContentType())
                            .build()
            );
            is.close();
            return url;
        }catch (MinioException |IOException | NoSuchAlgorithmException | InvalidKeyException e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
