package com.xietong.demo.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MethodLogAdvice {
    private final static Logger logger = Logger.getLogger(MethodLogAdvice.class);

    @Around("com.xietong.demo.aspect.SystemArchitecture.businessService()")
    public Object doServiceLogActions(ProceedingJoinPoint pjp) throws Throwable {
        return doLogAction(pjp);
    }

    @Around("com.xietong.demo.aspect.SystemArchitecture.inInfrastructureLayer()")
    public Object doInfrastructureLogActions(ProceedingJoinPoint pjp) throws Throwable {
        return doLogAction(pjp);
    }

    private Object doLogAction(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String className = pjp.getTarget().getClass().getSimpleName();
        if (pjp.getArgs()==null||pjp.getArgs().length==0){
            logger.info("className:"+className+" call method:"+ method.getName());
        }else{
            for(final Object argument : pjp.getArgs()){
                logger.info("className:"+className+" call method:"+ method.getName()+" parameter:" + argument);
            }
        }
        Object retVal = pjp.proceed();
        return retVal;
    }
}
