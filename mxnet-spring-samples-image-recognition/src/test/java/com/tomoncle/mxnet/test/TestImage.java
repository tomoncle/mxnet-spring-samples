package com.tomoncle.mxnet.test;


import org.apache.mxnet.javaapi.Image;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestImage {

    @Test
    public void t1() throws Exception {

        String inputImagePath = "/home/tomoncle/workspace/java/github.com/tomoncle/mxnet-spring-samples/models/" +
                "resnet50_ssd/images/dog.jpg";
        BufferedImage buf = ImageIO.read(new File(inputImagePath));
        List<Map<String, Integer>> boxes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        names.add("test");

        int width = buf.getWidth();
        int height = buf.getHeight();
        // Covert to image
        Map<String, Integer> map = new HashMap<>();
        map.put("xmin", 312 * width / 512);
        map.put("xmax", 456 * width / 512);
        map.put("ymin", 72 * height / 512);
        map.put("ymax", 150 * height / 512);
        boxes.add(map);

        Image.drawBoundingBox(buf, boxes, names);
        File outputFile = new File("boundingImage.png");
        ImageIO.write(buf, "png", outputFile);
        System.exit(0);
    }

    @Test
    public void t2() {
        try {
            Files.createDirectories(Paths.get("/tmp/123/456/789"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}