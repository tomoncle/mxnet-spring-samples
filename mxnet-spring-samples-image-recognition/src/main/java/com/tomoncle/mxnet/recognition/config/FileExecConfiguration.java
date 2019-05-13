package com.tomoncle.mxnet.recognition.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mxnet.storage.file")
public class FileExecConfiguration {
    // files upload dir
    private String upload = "/tmp/mxnet/upload";
    // files download dir
    private String download = "/tmp/mxnet/download";


}
