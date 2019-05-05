package com.tomoncle.mxnet.recognition.detect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.mxnet.Layout;
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
 * @Time : 2019/4/23 21:07
 * @Author : TOM.LEE
 * @File : ImageFile.java
 * @Docs : https://medium.com/apache-mxnet/introducing-java-apis-for-deep-learning-inference-with-apache-mxnet-8406a698fa5a
 */
public class ImageFileDetection {

    /**
     * 通过在上下文对象中指定inference，可以很容易地指定是要在CPU上还是GPU（如果您有支持GPU的计算机）上运行inference。
     *
     * @return
     */
    private static List<Context> getContext() {
        List<Context> ctx = new ArrayList<>();
        ctx.add(Context.cpu());
        // For GPU, context.add(Context.gpu());
        return ctx;
    }

    private static String output(List<List<ObjectDetectorOutput>> output, Shape inputShape) {
        int width = inputShape.get(3);
        int height = inputShape.get(2);
        JSONArray result = new JSONArray();

        for (List<ObjectDetectorOutput> ele : output) {
            JSONArray jsonArray = new JSONArray();
            for (ObjectDetectorOutput i : ele) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("class", i.getClassName());
                jsonObject.put("probability", i.getProbability());
                // 这里是按512*512 像素返回的位置值,如果要画图的图片不是512*512像素,
                // 那需要 xmin(xmax) * image.width/512,  ymin(ymax) * image.height/512,
                List<Float> locations = Arrays.asList(
                        i.getXMin() * width,
                        i.getXMax() * width,
                        i.getYMin() * height,
                        i.getYMax() * height);
                jsonObject.put("location", locations);
                jsonArray.add(jsonObject);
            }
            result.add(jsonArray);
        }

        return result.toJSONString();

    }


    public String InputImage(String inputImagePath, String modelPathPrefix) {
        List<Context> context = getContext();
        // 1表示批量大小，在我们的例子中是单个图像。
        // 3用于图像中的通道，对于RGB图像为3
        // 512代表图像的高度和宽度
        Shape inputShape = new Shape(new int[]{1, 3, 512, 512});
        List<DataDesc> inputDescriptors = new ArrayList<>();
        // 布局指定给定的形状是基于NCHW的，NCHW是批大小、通道大小、高度和宽度
        // dtype是图像数据类型，它将是标准float32
        inputDescriptors.add(new DataDesc("data", inputShape, DType.Float32(), Layout.NCHW()));
        BufferedImage img = ObjectDetector.loadImageFromFile(inputImagePath);
        ObjectDetector objDet = new ObjectDetector(modelPathPrefix, inputDescriptors, context, 0);
        // 3表示获取识别率最高的三个对象
        List<List<ObjectDetectorOutput>> output = objDet.imageObjectDetect(img, 3);
        return output(output, inputShape);
    }

}
