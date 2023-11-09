package com.project.animal.global.common.provider.inf;

public interface AuthCodeProvider {

    void generateAuthCode(String email);

    boolean validateAuthCode(String email, String token);


}
