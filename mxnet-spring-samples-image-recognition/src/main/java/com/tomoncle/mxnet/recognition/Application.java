package com.tomoncle.mxnet.recognition;


import com.tomoncle.mxnet.recognition.config.SSDModelConfiguration;
import com.tomoncle.mxnet.recognition.detect.ImageFileDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main class
 *
 * Created by Administrator on 2019/4/23.
 */

@RestController
@SpringBootApplication(scanBasePackages = {"com.tomoncle.mxnet.recognition"})
public class Application extends SpringBootServletInitializer {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    SSDModelConfiguration config;

    @RequestMapping("/test")
    public String test() {
        return new ImageFileDetection().InputImage(config.imagePath(), config.modelPrefix());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext application = SpringApplication.run(Application.class, args);
        if (logger.isDebugEnabled()) {
            String[] beanDefinitionNames = application.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                logger.debug(beanName);
            }
        }
//        logger.info("Started HTTP server on [::]:" + System.getProperty("server.port"));
    }


}
