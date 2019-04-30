package com.tomoncle.mxnet.recognition.api;

import com.tomoncle.mxnet.recognition.config.SSDModelConfiguration;
import com.tomoncle.mxnet.recognition.detect.ImageFileDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @Autowired
    SSDModelConfiguration config;

    @RequestMapping("/testDog")
    public String testDog() {
        return new ImageFileDetection().InputImage(config.dogImagePath(), config.modelPrefix());
    }

    @RequestMapping("/testPerson")
    public String testPerson() {
        return new ImageFileDetection().InputImage(config.personImagePath(), config.modelPrefix());
    }
}
