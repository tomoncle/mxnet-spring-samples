package com.tomoncle.mxnet.recognition.detect;

import com.alibaba.fastjson.JSONArray;
import org.apache.mxnet.javaapi.Image;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class FileMXNetImageTools {

    /**
     * 返回标注识别对象的图片
     *
     * @param path                    图片路径
     * @param imageDetectionJSONArray mxnet识别的标注信息
     * @param preserve                是否保存到本地
     * @return InputStream
     * @throws IOException
     */
    public static InputStream drawImage(
            String path, JSONArray imageDetectionJSONArray, boolean preserve) throws IOException {
        // get detected image buf
        BufferedImage buf = buildBufferImage(path, imageDetectionJSONArray);

        // save file to disk
        if (preserve) {
            final String fileType = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
            final String fileName = path.substring(0, path.lastIndexOf(".")) + "-detect";
            File outputFile = new File(String.format("%s.%s",fileName,fileType));
            ImageIO.write(buf, fileType, outputFile);
        }

        // save to InputStream
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(bs);
        ImageIO.write(buf, "png", ios);
//        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        return new ByteArrayInputStream(bs.toByteArray());
    }

    /**
     * 返回标注识别对象的图片
     *
     * @param path                    图片路径
     * @param imageDetectionJSONArray mxnet识别的标注信息
     * @return InputStream
     * @throws IOException
     */
    public static InputStream drawImage(
            String path, JSONArray imageDetectionJSONArray) throws IOException {
        return drawImage(path,imageDetectionJSONArray,false);
    }

    /**
     * 识别对象,并标注坐标,然后将识别后的文件信息保存到本地
     * @param path 文件路径
     * @param imageDetectionJSONArray mxnet识别的标注信息
     * @return
     * @throws IOException
     */
    public static String loadImage(String path, JSONArray imageDetectionJSONArray) throws IOException {
        BufferedImage buf = buildBufferImage(path, imageDetectionJSONArray);
        final String fileType = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
        final String fileOldName = path.substring(0, path.lastIndexOf(".")) + "-detect";
        final String fileNewName = String.format("%s.%s",fileOldName,fileType);
        File outputFile = new File(fileNewName);
        ImageIO.write(buf, fileType, outputFile);
        return fileNewName;
    }

    /**
     * 识别对象,并标注坐标,然后将识别后的文件信息写入BufferedImage对象
     * @param path 文件路径
     * @param imageDetectionJSONArray  mxnet识别的标注信息
     * @return
     * @throws IOException
     */
    private static BufferedImage buildBufferImage(
            String path, JSONArray imageDetectionJSONArray) throws IOException {
        // init detected box list
        List<Map<String, Integer>> boxes = new ArrayList<>();
        // init detected name list
        List<String> names = new ArrayList<>();
        // read input image file
        BufferedImage buf = ImageIO.read(new File(path));
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
