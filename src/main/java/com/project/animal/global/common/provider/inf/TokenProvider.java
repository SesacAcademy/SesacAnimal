package com.project.animal.global.common.provider.inf;

import com.project.animal.global.common.dto.MemberDto;
import javax.servlet.http.HttpServletRequest;

public interface TokenProvider {
    String generateToken(MemberDto member, String type);

    void removeToken(MemberDto member);

    boolean matchToken(String token);

    MemberDto parseToken(String token);

    String resolveToken(HttpServletRequest request, String type);

    boolean validateToken(String token);
}
