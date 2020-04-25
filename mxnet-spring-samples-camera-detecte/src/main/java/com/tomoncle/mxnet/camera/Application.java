package com.tomoncle.mxnet.camera;

import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;


public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    // model path prefix
    private static final String modelPathPrefix = "/home/tomoncle/mxnet-models/resnet50_ssd/resnet50_ssd_model";

    private static Frame frameProcessor(Frame frame) {
        FrameConverter<BufferedImage> frameConverter = new Java2DFrameConverter();
        BufferedImage bufferedImage = frameConverter.convert(frame);
        bufferedImage = ImageFileDetection.inputImage(bufferedImage, modelPathPrefix);
        return frameConverter.convert(bufferedImage);
    }


    public static void main(String[] args) throws FrameGrabber.Exception, InterruptedException {
        logger.debug("启动程序");
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("camera");//新建一个窗口
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        while (true) {
            if (!canvas.isDisplayable()) {//窗口是否关闭
                logger.debug("启动退出");
                grabber.stop();//停止抓取
                System.exit(-1);//退出
            }
            Frame frame = grabber.grab(); //这里的Frame frame=grabber.grab(); frame是一帧视频图像
            frame = frameProcessor(frame);
            canvas.showImage(frame);//获取摄像头图像并放到窗口上显示，
            Thread.sleep(50); //50毫秒刷新一次图像
        }
    }

}
