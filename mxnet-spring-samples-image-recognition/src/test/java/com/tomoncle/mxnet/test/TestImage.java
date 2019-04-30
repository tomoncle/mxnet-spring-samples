package com.tomoncle.mxnet.test;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class TestImage {

    public static void main(String[] args) throws Exception{

            String inputImagePath = "/home/tomoncle/workspace/java/github.com/tomoncle/mxnet-spring-samples/models/" +
                    "resnet50_ssd/images/dog.jpg";
            BufferedImage buf = ImageIO.read(new File(inputImagePath));
            List<Map<String, Integer>> boxes = new ArrayList<>();
            List<String> names = new ArrayList<>();
            // Covert to image
            Image.drawBoundingBox(buf, boxes, names);
            File outputFile = new File("boundingImage.png");
            ImageIO.write(buf, "png", outputFile);
            System.exit(0);
    }

}