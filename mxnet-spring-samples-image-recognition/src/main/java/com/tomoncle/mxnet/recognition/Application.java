package com.tomoncle.mxnet.recognition;


import com.tomoncle.config.springboot.EnableSpringBootConfig;
import com.tomoncle.config.springboot.constant.SpringBootConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

/**
 * Main class
 * <p/>
 * Created by Administrator on 2019/4/23.
 */


@SpringBootApplication(
        scanBasePackages = {Application.SCAN_PROJECT_PACKAGE},
        scanBasePackageClasses = {EnableSpringBootConfig.class}
)
public class Application extends SpringBootServletInitializer {

    static final String SCAN_PROJECT_PACKAGE = "com.tomoncle.mxnet.recognition";

    private static Logger logger = LoggerFactory.getLogger(Application.class);


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        ApplicationContext application = SpringApplication.run(Application.class, args);
        if (logger.isDebugEnabled()) {
            String[] beanDefinitionNames = application.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                logger.debug(beanName);
            }
        }
    }


}
