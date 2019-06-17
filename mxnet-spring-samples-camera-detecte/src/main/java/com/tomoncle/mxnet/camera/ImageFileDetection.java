package com.tomoncle.mxnet.camera;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.mxnet.Layout;
import org.apache.mxnet.infer.javaapi.ObjectDetector;
import org.apache.mxnet.infer.javaapi.ObjectDetectorOutput;
import org.apache.mxnet.javaapi.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @Time : 2019/4/23 21:07
 * @Author : TOM.LEE
 * @File : ImageFile.java
 * <p>
 * Reference Of :
 * https://medium.com/apache-mxnet/introducing-java-apis-for-deep-learning-inference-with-apache-mxnet-8406a698fa5a
 * https://github.com/apache/incubator-mxnet/blob/master/scala-package/examples/src/main/java/org/apache/mxnetexamples/javaapi/infer/objectdetector/SSDClassifierExample.java
 */
public class ImageFileDetection {


    // 如果识别准确率小于0.5则忽略
    private static final float IGNORE_PROBABILITY = 0.5f;
    // 通过在上下文对象中指定inference，可以很容易地指定是要在CPU上还是GPU（如果您有支持GPU的计算机）上运行inference。
    private static final List<Context> ctx = new ArrayList<>();

    static {
        if (System.getenv().containsKey("SCALA_TEST_ON_GPU") &&
                Integer.valueOf(System.getenv("SCALA_TEST_ON_GPU")) == 1) {
            ctx.add(Context.gpu());
            System.out.println("使用GPU");
        } else {
            ctx.add(Context.cpu());
            System.out.println("使用CPU");
        }
    }


    private static String output(List<List<ObjectDetectorOutput>> output, Shape inputShape) {
        int width = inputShape.get(3);
        int height = inputShape.get(2);
        JSONArray result = new JSONArray();

        for (List<ObjectDetectorOutput> ele : output) {
            JSONArray jsonArray = new JSONArray();
            for (ObjectDetectorOutput i : ele) {
                if (i.getProbability() < IGNORE_PROBABILITY) {
                    // 如果识别准确率小于0.5则跳过
                    continue;
                }
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


    public static BufferedImage inputImage(BufferedImage img, String modelPathPrefix) {
//        List<Context> context = getContext();
        // 1表示批量大小，在我们的例子中是单个图像。
        // 3用于图像中的通道，对于RGB图像为3
        // 512代表图像的高度和宽度
        Shape inputShape = new Shape(new int[]{1, 3, 512, 512});
        List<DataDesc> inputDescriptors = new ArrayList<>();
        // 布局指定给定的形状是基于NCHW的，NCHW是批大小、通道大小、高度和宽度
        // dtype是图像数据类型，它将是标准float32
        inputDescriptors.add(new DataDesc("data", inputShape, DType.Float32(), Layout.NCHW()));
        ObjectDetector objDet = new ObjectDetector(modelPathPrefix, inputDescriptors, ctx, 0);
        // topK=5表示获取识别率最高的5个对象
        final int topK = 5;
        List<List<ObjectDetectorOutput>> output = objDet.imageObjectDetect(img, topK);
        String json = output(output, inputShape);
        JSONArray jsonArray = JSONArray.parseArray(json);
        return buildBufferImage(img, jsonArray.getJSONArray(0));
    }

    /**
     * 识别对象,并标注坐标,然后将识别后的文件信息写入BufferedImage对象
     *
     * @param buf                     read input image file
     * @param imageDetectionJSONArray mxnet识别的标注信息
     * @return
     * @throws IOException
     */
    private static BufferedImage buildBufferImage(BufferedImage buf, JSONArray imageDetectionJSONArray) {
        // init detected box list
        List<Map<String, Integer>> boxes = new ArrayList<>();
        // init detected name list
        List<String> names = new ArrayList<>();
        // get image width & height
        int width = buf.getWidth();
        int height = buf.getHeight();

        IntStream.range(0, imageDetectionJSONArray.size())
                .mapToObj(imageDetectionJSONArray::getJSONObject)
                .forEach(jsonObject -> {
                    names.add(String.format("%s: %s",
                            jsonObject.getString("class"), jsonObject.getString("probability")));
                    Map<String, Integer> map = new HashMap<>();
                    Object[] locations = jsonObject.getJSONArray("location").toArray();
                    map.put("xmin", Float.valueOf(locations[0].toString()).intValue() * width / 512);
                    map.put("xmax", Float.valueOf(locations[1].toString()).intValue() * width / 512);
                    map.put("ymin", Float.valueOf(locations[2].toString()).intValue() * height / 512);
                    map.put("ymax", Float.valueOf(locations[3].toString()).intValue() * height / 512);
                    boxes.add(map);
                });
        // add boxes and names to buf
        Image.drawBoundingBox(buf, boxes, names);
        return buf;
    }

}
