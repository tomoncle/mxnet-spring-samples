package com.tomoncle.mxnet.recognition.common.file;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileExecService extends FileSystemService {

    /**
     * @param file
     */
    void save(MultipartFile file);

    /**
     * 获取目录文件列表
     *
     * @return
     */
    Stream<Path> loadAll();


    /**
     * 获取文件
     *
     * @param filename file full path
     * @return
     */
    Path load(String filename);


    /**
     * 获取文件为 Resource
     *
     * @param filename
     * @return
     */
    Resource loadAsResource(String filename);


}
