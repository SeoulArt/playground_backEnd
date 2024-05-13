//package com.skybory.seoulArt.global;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//public class FileUploadController {
//    private final FileUploadService fileUploadService;
//
//    public FileUploadController(FileUploadService fileUploadService) {
//        this.fileUploadService = fileUploadService;
//    }
//
//    @PostMapping("/upload")
//    public String uploadFile(@RequestParam("file") MultipartFile file) {
//        return fileUploadService.uploadFile(file, null);
//    }
//}