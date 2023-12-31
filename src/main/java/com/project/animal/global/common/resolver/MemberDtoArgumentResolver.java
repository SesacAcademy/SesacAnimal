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

import static com.project.animal.global.common.constant.TokenTypeValue.*;

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

        // 쿠키에 저장된 JWT Access 토큰 가져오기
        String token = jwtTokenProvider.resolveToken(request, JWT_ACCESS_TOKEN);

        // 기존 쿠키에 JWT Access 토큰이 없는 경우, Request 영역에 저장해둔 newAccessToken을 사용
        if (token == null && request.getAttribute(JWT_ACCESS_TOKEN) != null)
            token = (String) request.getAttribute(JWT_ACCESS_TOKEN);

        // JWT Access 토큰을 파싱하여 MemberDto 객체로 리턴
        if (token != null)
            return jwtTokenProvider.parseToken(token);

        // 없으면 null 값 리턴
        return null;
    }
}
