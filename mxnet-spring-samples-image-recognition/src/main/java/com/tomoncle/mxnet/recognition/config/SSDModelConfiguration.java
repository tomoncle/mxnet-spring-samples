package com.tomoncle.mxnet.recognition.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Time : 2019/4/23 20:51
 * @Author : TOM.LEE
 * @File : SSDModelConfiguration.java
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "model.ssd")
public class SSDModelConfiguration {

    // download model dir path
    private String root;

    /**
     * 获取模型前缀
     *
     * @return
     */
    public String modelPrefix() {
        return String.format("%s/resnet50_ssd/resnet50_ssd_model", this.root);
    }

    /**
     * 获取图片路径
     *
     * @return
     */
    public String dogImagePath() {
        return String.format("%s/resnet50_ssd/images/dog.jpg", this.root);
    }

    /**
     * 获取图片路径
     *
     * @return
     */
    public String personImagePath() {
        return String.format("%s/resnet50_ssd/images/person.jpg", this.root);
    }

}
