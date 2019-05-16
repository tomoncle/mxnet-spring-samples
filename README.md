# mxnet-spring-samples 
[![Build Status](https://travis-ci.org/tomoncle/mxnet-spring-samples.svg?branch=master)][travis] ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/tomoncle/mxnet-spring-samples.svg) ![GitHub repo size](https://img.shields.io/github/repo-size/tomoncle/mxnet-spring-samples.svg?color=green&logoColor=green) ![GitHub top language](https://img.shields.io/github/languages/top/tomoncle/mxnet-spring-samples.svg?color=yes) ![GitHub issues](https://img.shields.io/github/issues/tomoncle/mxnet-spring-samples.svg) ![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)

Apache MXNet (Incubating) A flexible and efficient library for deep learning for Java samples.

## mxnet-spring-samples-image-recognition
springboot and Apache MXNet Image recognition and DrawBoxes.
* Run
  * download model: [dl_ssd_model.sh](models/dl_ssd_model.sh)
  * update config : [application.properties](mxnet-spring-samples-image-recognition/src/main/resources/application.properties)
  * boot MainClass:`com.tomoncle.mxnet.recognition.Application.java`

## Demonstration
 > Identify uploaded objects, label types, and locate their exact coordinates.
##### 1.select the image to upload
![...](https://raw.githubusercontent.com/tomoncle/img/master/face-detection-induction-course/person.jpg)

##### 2.upload your image
![...](https://raw.githubusercontent.com/tomoncle/img/master/face-detection-induction-course/1.png)

##### 3.waiting to upload and return the processed image
![...](https://raw.githubusercontent.com/tomoncle/img/master/face-detection-induction-course/view.png)

## For docker run: 
```bash
$ docker run -d -p 9080:9080 tomoncleshare/face-detection-induction-course:20190512
```

##### Find code : [mxnet-spring-samples-image-recognition](mxnet-spring-samples-image-recognition) .
[travis]: https://travis-ci.org/tomoncle/mxnet-spring-samples
