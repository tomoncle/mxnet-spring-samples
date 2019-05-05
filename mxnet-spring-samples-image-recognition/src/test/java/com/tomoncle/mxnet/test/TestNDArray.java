package com.tomoncle.mxnet.test;


import org.apache.mxnet.Layout;
import org.apache.mxnet.infer.javaapi.ObjectDetector;
import org.apache.mxnet.infer.javaapi.ObjectDetectorOutput;
import org.apache.mxnet.infer.javaapi.Predictor;
import org.apache.mxnet.javaapi.Context;
import org.apache.mxnet.javaapi.DType;
import org.apache.mxnet.javaapi.DataDesc;
import org.apache.mxnet.javaapi.Shape;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestNDArray {

    static final String modelPathPrefix = "/home/tomoncle/workspace/java/github.com/tomoncle/mxnet-spring-samples/models/" +
            "resnet50_ssd/resnet50_ssd_model";

    static final String inputImagePath = "/home/tomoncle/workspace/java/github.com/tomoncle/mxnet-spring-samples/models/" +
            "resnet50_ssd/images/dog.jpg";

    @Test
    public void testImage() {
        List<Context> context = new ArrayList<>();
        context.add(Context.cpu());

        Shape inputShape = new Shape(new int[]{1, 3, 512, 512});

        List<DataDesc> inputDescriptors = new ArrayList<>();

        inputDescriptors.add(new DataDesc("data", inputShape, DType.Float32(), Layout.NCHW()));

        BufferedImage img = ObjectDetector.loadImageFromFile(inputImagePath);
        ObjectDetector objDet = new ObjectDetector(modelPathPrefix, inputDescriptors, context, 0);
        List<List<ObjectDetectorOutput>> output = objDet.imageObjectDetect(img, 3);

        StringBuilder outputStr = new StringBuilder();

        int width = inputShape.get(3);
        int height = inputShape.get(2);

        for (List<ObjectDetectorOutput> ele : output) {
            for (ObjectDetectorOutput i : ele) {
                outputStr.append("Class: ").append(i.getClassName()).append("\n");
                outputStr.append("Probabilties: ").append(i.getProbability()).append("\n");

                List<Float> coord = Arrays.asList(i.getXMin() * width,
                        i.getXMax() * height, i.getYMin() * width, i.getYMax() * height);
                StringBuilder sb = new StringBuilder();
                for (float c : coord) {
                    sb.append(", ").append(c);
                }
                outputStr.append("Coord:").append(sb.substring(2)).append("\n");
            }
        }
        System.out.println(outputStr);


    }


    @Test
    public void testPredictor() {
        List<Context> context = new ArrayList<>();
        if (System.getenv().containsKey("SCALA_TEST_ON_GPU") &&
                Integer.valueOf(System.getenv("SCALA_TEST_ON_GPU")) == 1) {
            context.add(Context.gpu());
        } else {
            context.add(Context.cpu());
        }
        List<DataDesc> inputDesc = new ArrayList<>();
        Shape inputShape = new Shape(new int[]{1, 3, 224, 224});
        inputDesc.add(new DataDesc("data", inputShape, DType.Float32(), Layout.NCHW()));
        Predictor predictor = new Predictor(modelPathPrefix, inputDesc, context, 0);
        // Prepare data
        System.out.println(predictor.predictor());

    }


    @Test
    public  void  testArray(){
        Object[] locations = {"312.1234","45.12345"};
        Integer a =Float.valueOf(locations[0].toString()).intValue();
        System.out.println(a);
    }

}
