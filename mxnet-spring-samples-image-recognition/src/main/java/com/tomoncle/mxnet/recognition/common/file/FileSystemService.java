package com.tomoncle.mxnet.recognition.common.file;

public interface FileSystemService {

    void createDir(String dirName);

    void deleteDir(String dirName);

    boolean exists(String path);

}
