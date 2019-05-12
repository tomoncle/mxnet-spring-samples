package com.tomoncle.mxnet.test;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.GaussianBlur;

/**
 * @Time : 2019/5/12 20:49
 * @Author : TOM.LEE
 * @File : TestJavaCV.java
 */
public class TestJavaCV {
    public static void smooth(String filename) {
        Mat image = imread(filename);
        if (image != null) {
            GaussianBlur(image, image, new Size(3, 3), 0);
            imwrite("h.jpg", image);
        }
    }

    public static void main(String[] args) {
        smooth("C:\\Users\\Administrator\\Desktop\\" +
                "ImageMagick-6.9.10-33-portable-Q16-x86" +
                "\\images\\red-ball.png");
    }


}
