package com.tomoncle.mxnet.recognition;

import org.apache.mxnet.infer.javaapi.ObjectDetector;
import org.apache.mxnet.infer.javaapi.ObjectDetectorOutput;
import org.apache.mxnet.javaapi.Context;
import org.apache.mxnet.javaapi.DType;
import org.apache.mxnet.javaapi.DataDesc;
import org.apache.mxnet.javaapi.Shape;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * app main
 * Created by Administrator on 2019/4/23.
 */
public class Application {

    public static void main(String[] args) {

        String modelRootPath = getModelRootPath();
        String modelPathPrefix = String.format("%s/resnet50_ssd/resnet50_ssd_model", modelRootPath);
        String inputImagePath = String.format("%s/resnet50_ssd/images/dog.jpg", modelRootPath);

        if (isWindows()) {
            modelPathPrefix = String.format("%s\\resnet50_ssd\\resnet50_ssd_model", modelRootPath);
            inputImagePath = String.format("%s\\resnet50_ssd\\images\\dog.jpg", modelRootPath);
        }



        List<Context> context = getContext();

        Shape inputShape = new Shape(new int[] {1, 3, 512, 512});

        List<DataDesc> inputDescriptors = new ArrayList<DataDesc>();
        inputDescriptors.add(new DataDesc("data", inputShape, DType.Float32(), "NCHW"));

        BufferedImage img = ObjectDetector.loadImageFromFile(inputImagePath);
        ObjectDetector objDet = new ObjectDetector(modelPathPrefix, inputDescriptors, context, 0);
        List<List<ObjectDetectorOutput>> output = objDet.imageObjectDetect(img, 3);

        printOutput(output, inputShape);
    }

    /**
     * 判断是否为windows 操作系统
     * @return
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    /**
     * 获取模型文件夹路径
     *
     * @return
     */
    private static String getModelRootPath() {
        if (isWindows()) {
            return String.format("%s\\models", System.getProperty("user.dir"));
        }
        return String.format("%s/models", System.getProperty("user.dir"));
    }


    private static List<Context> getContext() {
        List<Context> ctx = new ArrayList<>();
        ctx.add(Context.cpu());

        return ctx;
    }

    private static void printOutput(List<List<ObjectDetectorOutput>> output, Shape inputShape) {

        StringBuilder outputStr = new StringBuilder();

        int width = inputShape.get(3);
        int height = inputShape.get(2);

        for (List<ObjectDetectorOutput> ele : output) {
            for (ObjectDetectorOutput i : ele) {
                outputStr.append("Class: " + i.getClassName() + "\n");
                outputStr.append("Probabilties: " + i.getProbability() + "\n");

                List<Float> coord = Arrays.asList(i.getXMin() * width,
                        i.getXMax() * height, i.getYMin() * width, i.getYMax() * height);
                StringBuilder sb = new StringBuilder();
                for (float c : coord) {
                    sb.append(", ").append(c);
                }
                outputStr.append("Coord:" + sb.substring(2) + "\n");
            }
        }
        System.out.println(outputStr);

    }
}
