package com.project.animal.global.common.resolver;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDtoArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberAnnotation = parameter.hasParameterAnnotation(Member.class);
        boolean hasMemberType = MemberDto.class.isAssignableFrom(parameter.getParameterType());

        return hasMemberAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = jwtTokenProvider.resolveToken(request, "accessToken");

        // 기존 쿠키에 JWT Access 토큰이 없는 경우, Request 영역에 저장해둔 newAccessToken을 사용
        if (token == null && request.getAttribute("accessToken") != null)
            token = (String) request.getAttribute("accessToken");

        // 기존 쿠키에 JWT Access 토큰이 있는 경우, JWT를 파싱하여 MemberDto 객체로 리턴
        if (token != null) {
            MemberDto member = new MemberDto();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProvider.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            member.setEmail(claims.getSubject());
            member.setId(Long.parseLong(((String) claims.get("uid"))));
            member.setRole(Enum.valueOf(Role.class, (String) claims.get("role")));

            return member;
        }
        
        // 없으면 null 값 리턴
        return null;
    }
}
