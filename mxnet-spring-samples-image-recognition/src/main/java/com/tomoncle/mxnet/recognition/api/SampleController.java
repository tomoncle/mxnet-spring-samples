package com.tomoncle.mxnet.recognition.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tomoncle.mxnet.recognition.config.SSDModelConfiguration;
import com.tomoncle.mxnet.recognition.detect.ImageFileDetection;
import org.apache.mxnet.javaapi.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SampleController {
    @Autowired
    SSDModelConfiguration config;

    @RequestMapping("/testDog")
    public String testDog() {
        return new ImageFileDetection().InputImage(config.dogImagePath(), config.modelPrefix());
    }

    @RequestMapping("/dlDogJpg")
    public ResponseEntity<InputStreamResource> excelCustomersReport() throws IOException {
        String inputImagePath = config.dogImagePath();
        String json = new ImageFileDetection().InputImage(config.dogImagePath(), config.modelPrefix());
        JSONArray jsonArray = JSONArray.parseArray(json);

        List<Map<String, Integer>> boxes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        // image read
        BufferedImage buf = ImageIO.read(new File(inputImagePath));
        Image.drawBoundingBox(buf, boxes, names);
        int width = buf.getWidth();
        int height = buf.getHeight();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray deepArray = jsonArray.getJSONArray(i);
            for (int j = 0; j < deepArray.size(); j++) {
                JSONObject jsonObject = deepArray.getJSONObject(j);
                names.add(jsonObject.getString("class") + ": "
                        + jsonObject.getString("probability"));

                Map<String, Integer> map = new HashMap<>();
                Object[] locations = jsonObject.getJSONArray("location").toArray();
                map.put("xmin", Float.valueOf(locations[0].toString()).intValue() * width / 512);
                map.put("xmax", Float.valueOf(locations[1].toString()).intValue() * width / 512);
                map.put("ymin", Float.valueOf(locations[2].toString()).intValue() * height / 512);
                map.put("ymax", Float.valueOf(locations[3].toString()).intValue() * height / 512);
                boxes.add(map);
            }

        }
        // add boxes and names to buf
        Image.drawBoundingBox(buf, boxes, names);
        // save to file
        File outputFile = new File("dog.png");
        ImageIO.write(buf, "png", outputFile);

        // save to InputStream
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(bs);
        ImageIO.write(buf, "png", ios);
        InputStream is = new ByteArrayInputStream(bs.toByteArray());

        // set http response
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=dog.png");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(is));
    }


    @RequestMapping("/testPerson")
    public String testPerson() {
        return new ImageFileDetection().InputImage(config.personImagePath(), config.modelPrefix());
    }


}
