package com.project.animal.global.common.dto;

import com.project.animal.global.common.minioserviceprovider.minioException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ImageListDto {
    private List<MultipartFile> imageList;
    private int imageCount;

    public ImageListDto(List<MultipartFile> imageList) {
        this.imageList = imageList;
        this.imageCount = imageList.size();
        if (imageCount>5){
            throw new minioException("이미지를 5장 이상 초과할 수 없습니다.");
        }
        //화면에서 입력한 이미지의 숫자와 리스트의 숫자를 비교하는 메소드를 위한 생성자
        //화면에서 입력한 이미지의 숫자를 컨트롤러에서 받고 비교 현재 화면에서 사용자가 입력한 이미지 갯수를 입력받을 수 없어 list.size()로 임의 작성
    }
}
