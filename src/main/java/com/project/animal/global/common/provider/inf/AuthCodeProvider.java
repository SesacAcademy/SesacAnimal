package com.project.animal.global.common.provider.inf;

public interface AuthCodeProvider {
    void generateAuthCode(String to);

    boolean validateAuthCode(String to, String authCode);
}
