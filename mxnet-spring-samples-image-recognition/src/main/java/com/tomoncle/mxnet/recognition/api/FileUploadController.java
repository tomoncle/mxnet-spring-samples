package com.tomoncle.mxnet.recognition.api;

import com.alibaba.fastjson.JSONArray;
import com.tomoncle.config.springboot.utils.file.LocalFileService;
import com.tomoncle.config.springboot.utils.file.LocalFileServiceImpl;
import com.tomoncle.mxnet.recognition.config.FileExecConfiguration;
import com.tomoncle.mxnet.recognition.config.SSDModelConfiguration;
import com.tomoncle.mxnet.recognition.detect.FileMXNetImageTools;
import com.tomoncle.mxnet.recognition.detect.ImageFileDetection;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final LocalFileService fileUpload;

    private final SSDModelConfiguration ssdModelConfiguration;

    private final String UPLOAD_ROOT_PATH;

    public FileUploadController(FileExecConfiguration f, SSDModelConfiguration s) {
        fileUpload = new LocalFileServiceImpl(f.getUpload());
        UPLOAD_ROOT_PATH = f.getUpload();
        ssdModelConfiguration = s;
    }

    /**
     * 图片预览
     *
     * @param filename file name
     * @return
     */
    @GetMapping(value = "/{filename:.+}/view", produces = {
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public ResponseEntity<Resource> serveFileView(@PathVariable String filename) throws FileNotFoundException {
        Path load = fileUpload.load(filename);
        InputStream is = new FileInputStream(load.toFile());
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(is));
    }


    /**
     * 图片下载
     *
     * @param filename file name
     * @return
     */
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileUpload.loadAsResource(filename);
        // 强制浏览器下载 attachment; filename=
        // 浏览器预览 inline; filename=
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * upload and show results
     *
     * @param message 接受 handleFileUpload 方法通过 redirectAttributes 传递的参数
     * @return
     */
    @GetMapping
    public String uploadedFiles(@ModelAttribute("message") String message) {
        return " <!doctype html>\n" +
                "    <title>Upload new File</title>\n" +
                message + "\n" +
                "    <h1>Upload Have Face Image File</h1>\n" +
                "    <form method=post enctype=multipart/form-data>\n" +
                "      <input type=file name=file>\n" +
                "      <input type=submit value=Upload>\n" +
                "    </form>";
    }

    /**
     * 保存文件
     *
     * @param file               上传的文件
     * @param redirectAttributes 转发的属性
     * @return url for "/files"
     */
    @PostMapping
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
                                         RedirectAttributes redirectAttributes) {
        fileUpload.saveOrReplace(file);
        redirectAttributes.addFlashAttribute(
                "message", "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new ModelAndView("redirect:/files");
    }

    /**
     * 上传图片并预览识别后的图片
     *
     * @param file 上传的文件
     * @return url for "/files/{filename:.+}/view"
     */
    @PostMapping("/detectImage")
    public ModelAndView detectImage(@RequestParam("file") MultipartFile file) throws IOException {
        // save file
        fileUpload.saveOrReplace(file);
        // upload file path
        String filePath = String.format("%s/%s", UPLOAD_ROOT_PATH, file.getOriginalFilename());
        // detect json data
        String json = ImageFileDetection.inputImage(filePath, ssdModelConfiguration.modelPrefix());
        JSONArray jsonArray = JSONArray.parseArray(json);
        // detected save path
        filePath = FileMXNetImageTools.loadImage(filePath, jsonArray.getJSONArray(0));
        final String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        return new ModelAndView(String.format("redirect:/files/%s/view", fileName));
    }

    /**
     * 上传要识别的文件
     *
     * @return
     */
    @GetMapping("/detectImage")
    public String detectImage() {
        return " <!doctype html>\n" +
                "    <title>Upload new File</title>\n" +
                "    <h1>Upload Have Face Image File</h1>\n" +
                "    <form method=post enctype=multipart/form-data>\n" +
                "      <input type=file name=file>\n" +
                "      <input type=submit value=Upload>\n" +
                "    </form>";
    }


}
