package com.project.animal.adoption.service.inf;

import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionService {

    //조회수를 올려주기 위한 메소드
    public void plusView(Long id);
}
