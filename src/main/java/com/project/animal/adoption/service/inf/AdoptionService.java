package com.project.animal.adoption.service.inf;

import com.project.animal.sample.openApi.dto.OpenApiDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionService {

    //조회수를 올려주기 위한 메소드
    public void plusView(Long id);

    // openAPI를 저장하기 위한 메소드
    public void apiSave(List<OpenApiDto> openApiDtoList);
}
