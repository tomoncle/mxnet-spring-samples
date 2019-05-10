package com.tomoncle.mxnet.recognition.api;

import com.tomoncle.mxnet.recognition.common.file.FileUploadTools;
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
import java.io.InputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileUploadTools fileUpload;

    public FileUploadController(FileUploadTools fileUpload) {
        this.fileUpload = fileUpload;
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
    public String listUploadedFiles(@ModelAttribute("message") String message) {
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
        fileUpload.save(file);
        redirectAttributes.addFlashAttribute(
                "message", "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new ModelAndView("redirect:/files");
    }
}
