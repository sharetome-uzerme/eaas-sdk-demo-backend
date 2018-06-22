package com.xietong.demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Administrator on 2016/6/11.
 */
@Aspect
public class SystemArchitecture {

    @Pointcut("within(com.xietong.demo.eaas.facade.rest..*)")
    public void inRestAPILayer() {
    }

    @Pointcut("within(com.xietong.demo.eaas.service..*)")
    public void inServiceLayer() {
    }

    @Pointcut("within(com.xietong.demo.eaas.infrastructure..*)")
    public void inInfrastructureLayer() {
    }

    @Pointcut("execution(* com.xietong.demo.eaas..service.*.*(..))")
    public void businessService() {
    }
}

