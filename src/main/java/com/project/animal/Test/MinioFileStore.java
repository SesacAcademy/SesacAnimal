package com.project.animal.Test;

import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class MinioFileStore {

    /*private final MinioClient minioClient;

    public void save(BoardDto boardDto, MultipartFile file)  {
        // Bucket이 존재하는지 유무 체크
        try {
            if (!isBucketByUserId(boardDto.getAuthor())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(boardDto.getAuthor()).build());
            }

            // 서버에 저장될 파일 이름 생성
            String serverFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            InputStream fileData = file.getInputStream();

            // 파일 저장
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(boardDto.getAuthor())
                            .object(serverFileName)
                            .stream(fileData, file.getSize(), 0)
                            .build()
            );
            fileData.close();
            boardDto.setOriginalFileName(file.getOriginalFilename());
            boardDto.setServerFileName(serverFileName);

        } catch (Exception e) {
            new RuntimeException(e);
        }

    }

    public void delete(BoardDto boardDto) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // Bucket이 존재하는지 유무 체크
        if (!isBucketByUserId(boardDto.getAuthor())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(boardDto.getAuthor()).build());
        }

        // 파일 삭제
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(boardDto.getAuthor())
                        .object(boardDto.getServerFileName())
                        .build()
        );
    }

    public boolean isBucketByUserId(String userId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(userId).build());
    }*/
}
