package com.tomoncle.mxnet.recognition.api;

import com.alibaba.fastjson.JSONArray;
import com.tomoncle.mxnet.recognition.common.file.FileMXNetImageTools;
import com.tomoncle.mxnet.recognition.config.SSDModelConfiguration;
import com.tomoncle.mxnet.recognition.detect.ImageFileDetection;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/check")
public class SampleController {

    private final SSDModelConfiguration config;

    public SampleController(SSDModelConfiguration config) {
        this.config = config;
    }


    @GetMapping("/dog")
    public String testDog() {
        return ImageFileDetection.inputImage(config.dogImagePath(), config.modelPrefix());
    }

    @GetMapping("/dl/dog")
    public ResponseEntity<InputStreamResource> dlDogJpg() throws IOException {
        String inputImagePath = config.dogImagePath();
        String json = ImageFileDetection.inputImage(config.dogImagePath(), config.modelPrefix());
        JSONArray jsonArray = JSONArray.parseArray(json);

        InputStream is = FileMXNetImageTools.drawImage(inputImagePath, jsonArray.getJSONArray(0));
        // set http response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dog.png");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(is));
    }

    @GetMapping("/dl/person")
    public ResponseEntity<InputStreamResource> dlPersonJpg() throws IOException {
        String inputImagePath = config.personImagePath();
        String json = ImageFileDetection.inputImage(config.personImagePath(), config.modelPrefix());
        JSONArray jsonArray = JSONArray.parseArray(json);

        InputStream is = FileMXNetImageTools.drawImage(inputImagePath, jsonArray.getJSONArray(0));
        // set http response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=person.png");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(is));
    }

    @GetMapping("/person")
    public String testPerson() {
        return ImageFileDetection.inputImage(config.personImagePath(), config.modelPrefix());
    }


}
