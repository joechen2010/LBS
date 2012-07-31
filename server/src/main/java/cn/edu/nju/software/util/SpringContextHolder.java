package cn.edu.nju.software.util;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext = null;
    
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext; // NOSONAR
    }
    
    /*
     * @Override public void destroy() throws Exception { SpringContextHolder.cleanApplicationContext(); }
     */
    
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
        
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }
    
    public static <T> T getBean(Class<T> requiredType) {
        checkApplicationContext();
        return applicationContext.getBean(requiredType);
    }
    
    public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        checkApplicationContext();
        return applicationContext.getBeansOfType(requiredType);
    }
    
    public static void cleanApplicationContext() {
        applicationContext = null;
    }
    
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException(
                    "applicaitonContext not inject, please define SpringContextHolder in applicationContext.xml");
        }
    }
}
