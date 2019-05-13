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
     * @return InputStream
     * @throws IOException
     */
    public static InputStream drawImage(String path, JSONArray imageDetectionJSONArray) throws IOException {
        List<Map<String, Integer>> boxes = new ArrayList<>();
        List<String> names = new ArrayList<>();
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

        // save file to disk
//        File outputFile = new File("dog.png");
//        ImageIO.write(buf, "png", outputFile);

        // save to InputStream
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(bs);
        ImageIO.write(buf, "png", ios);
//        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        return new ByteArrayInputStream(bs.toByteArray());
    }

}
