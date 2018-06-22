package com.xietong.demo.aspect;

/**
 * Created by Administrator on 2016/6/2.
 */

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AfterThrowingAdvice {

    private final static Logger logger = Logger.getLogger(AfterThrowingAdvice.class);

    @AfterThrowing(pointcut = "com.xietong.demo.aspect.SystemArchitecture.inInfrastructureLayer()", throwing = "ex")
    public void doInfrastructureLogActions(Exception ex) {
        logger.error("general error ", ex);
    }

    @AfterThrowing(pointcut = "com.xietong.demo.aspect.SystemArchitecture.businessService()", throwing = "ex")
    public void doServiceLogActions(Exception ex) {
        logger.error(" general error ", ex);
    }
}
