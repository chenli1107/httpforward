package com.test.fwdcommon.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author 楚休红
 * @Date 2019/6/26
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextHolder.applicationContext == null) {
            SpringContextHolder.applicationContext = applicationContext;
        }
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    //通过Clazz返回指定的Beans
    public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

    //通过name，读取属性
    public static String getProperty(String name) {
        Environment environment = applicationContext.getEnvironment();
        return environment.getProperty(name);
    }
}
