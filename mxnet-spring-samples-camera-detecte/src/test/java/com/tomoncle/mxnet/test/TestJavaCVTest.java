package com.tomoncle.mxnet.test;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestJavaCVTest {

    private static OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

    @Test
    public void testCam() throws FrameGrabber.Exception, InterruptedException {
        //方式1
        //OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);

        //方式2
        VideoInputFrameGrabber grabber = VideoInputFrameGrabber.createDefault(0);

        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);

        int i = 0;
        while (true) {
            if (!canvas.isDisplayable()) {//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(-1);//退出
            }
            Frame frame = grabber.grab(); //这里的Frame frame=grabber.grab(); frame是一帧视频图像
            canvas.showImage(frame);//获取摄像头图像并放到窗口上显示，
            Thread.sleep(50);//50毫秒刷新一次图像
            i++;
            Mat mat = converter.convertToMat(grabber.grabFrame());
            opencv_imgcodecs.imwrite("D:\\tomoncle\\tmp\\" + i + ".png", mat);
        }
    }

    @Test
    public void testShowPicsOnCamera() throws FrameGrabber.Exception, InterruptedException {
        VideoInputFrameGrabber grabber = VideoInputFrameGrabber.createDefault(0);
        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);

        int i = 0;
        while (true) {
            if (!canvas.isDisplayable()) {//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(-1);//退出
            }
//            Frame frame = grabber.grab(); //这里的Frame frame=grabber.grab(); frame是一帧视频图像
            IplImage iplImage;
            if (i % 2 == 0) {
                iplImage = opencv_imgcodecs.cvLoadImage("D:\\workspace\\Java\\github.com\\tomoncle\\mxnet-spring-samples\\images\\PlsLabelMe1.jpg");
            } else {
                iplImage = opencv_imgcodecs.cvLoadImage("D:\\workspace\\Java\\github.com\\tomoncle\\mxnet-spring-samples\\images\\PlsLabelMe2.jpg");
            }
            Frame frame = converter.convert(iplImage);
            canvas.showImage(frame);//获取摄像头图像并放到窗口上显示，
            Thread.sleep(1000);//1000毫秒刷新一次图像
            i++;
        }

    }

    @Test
    public void testBufferedImageToFrame() throws IOException {
        FrameConverter<BufferedImage> frameConverter = new Java2DFrameConverter();
        File file = new File("D:\\workspace\\Java\\github.com\\tomoncle\\mxnet-spring-samples\\images\\PlsLabelMe1.jpg"); //本地图片
        BufferedImage image= ImageIO.read(file);
        Frame frame = frameConverter.convert(image);
        System.out.println(frame);
    }

    @Test
    public void testFrameToBufferedImage(){
        IplImage iplImage=opencv_imgcodecs.cvLoadImage("D:\\workspace\\Java\\github.com\\tomoncle\\mxnet-spring-samples\\images\\PlsLabelMe1.jpg");
        Frame frame = converter.convert(iplImage);
        FrameConverter<BufferedImage> frameConverter = new Java2DFrameConverter();
        BufferedImage bufferedImage = frameConverter.convert(frame);
        System.out.println(bufferedImage);
    }


    @Test
    public void testGPU(){
        System.out.println(System.getenv().containsKey("SCALA_TEST_ON_GPU"));
    }

}
