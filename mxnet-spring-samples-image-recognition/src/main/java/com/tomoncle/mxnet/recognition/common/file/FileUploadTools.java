package com.tomoncle.mxnet.recognition.common.file;

import com.tomoncle.mxnet.recognition.config.FileExecConfiguration;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * file exec utils
 * <p>
 * Reference Of https://github.com/spring-guides/gs-uploading-files
 */

@Component
public class FileUploadTools implements FileExecService {

    private final Path uploadDir;

    public FileUploadTools(FileExecConfiguration fileExecConfiguration) {
        this.uploadDir = Paths.get(fileExecConfiguration.getUpload());
        this.createDir(fileExecConfiguration.getUpload());
        this.createDir(fileExecConfiguration.getDownload());
    }

    @Override
    public void save(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new FileExecException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.uploadDir.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new FileExecException("Failed to system file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.uploadDir, 1)
                    .filter(path -> !path.equals(this.uploadDir))
                    .map(this.uploadDir::relativize);
        } catch (IOException e) {
            throw new FileExecException("Failed to read system files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return this.uploadDir.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileExecException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileExecException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void createDir(String dirName) {
        if (exists(dirName)) {
            return;
        }
        try {
            Files.createDirectories(Paths.get(dirName));
        } catch (IOException e) {
            throw new FileExecException("Could not create dir.", e);
        }
    }

    @Override
    public void deleteDir(String dirName) {
        if (exists(dirName)) {
            FileSystemUtils.deleteRecursively(Paths.get(dirName).toFile());
        }

    }

    @Override
    public boolean exists(String path) {
        if (Strings.isBlank(path)) {
            return false;
        }
        return Files.exists(Paths.get(path));
    }
}
