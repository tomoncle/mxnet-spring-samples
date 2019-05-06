package com.tomoncle.mxnet.recognition.api;

import com.tomoncle.mxnet.recognition.common.file.FileUploadTools;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileUploadTools fileUpload;

    public FileUploadController(FileUploadTools fileUpload) {
        this.fileUpload = fileUpload;
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileUpload.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping
    public String listUploadedFiles(Model model) {
        model.addAttribute("files", fileUpload.loadAll()
                .map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    /**
     * 保存文件
     *
     * @param file 上传的文件
     * @param redirectAttributes 转发的属性
     * @return url for "/files"
     */
    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        fileUpload.save(file);
        redirectAttributes.addFlashAttribute(
                "message", "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/files";
    }
}
