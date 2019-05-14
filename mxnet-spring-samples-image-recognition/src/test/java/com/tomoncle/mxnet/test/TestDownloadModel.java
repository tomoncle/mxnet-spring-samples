package com.tomoncle.mxnet.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Test Download.
 *      Reference Of : https://github.com/apache/incubator-mxnet/blob/master/scala-package/mxnet-demo/java-demo/src/main/java/mxnet/ImageClassification.java
 */
public class TestDownloadModel {

    private static void downloadUrl(String url, String filePath) {
        File tmpFile = new File(filePath);
        if (!tmpFile.exists()) {
            try {
                FileUtils.copyURLToFile(new URL(url), tmpFile);
            } catch (Exception exception) {
                System.err.println(exception.toString());
            }
        }
    }

    @Test
    public void downloadModelImage() {
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String baseUrl = "https://s3.us-east-2.amazonaws.com/scala-infer-models";

        downloadUrl(baseUrl + "/resnet-18/resnet-18-symbol.json",
                tempDirPath + "/resnet18/resnet-18-symbol.json");

        downloadUrl(baseUrl + "/resnet-18/synset.txt",
                tempDirPath + "/resnet18/synset.txt");

        downloadUrl("https://s3.amazonaws.com/model-server/inputs/Pug-Cookie.jpg",
                tempDirPath + "/inputImages/resnet18/Pug-Cookie.jpg");

        String modelPath = tempDirPath + File.separator + "resnet18/resnet-18";
        String imagePath = tempDirPath + File.separator + "inputImages/resnet18/Pug-Cookie.jpg";

        System.out.println(String.format("modelPath: %s\nimagePath: %s", modelPath, imagePath));
    }
}
