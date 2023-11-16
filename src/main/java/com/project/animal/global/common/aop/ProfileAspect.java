package com.project.animal.global.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ProfileAspect {

  @Pointcut("@within(com.project.animal.global.common.annotation.Profiling) || @annotation(com.project.animal.global.common.annotation.Profiling)")
  public void elapsedTimePointCut() {}

  @Around("elapsedTimePointCut()")
  public Object measureElapsedTime(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      return joinPoint.proceed();
    } finally {
      stopWatch.stop();
      long duration = stopWatch.getTotalTimeMillis();
      log.info(": >> executed in " + duration + " ms" + " << : " + joinPoint.getSignature());
    }

  }
}