package com.dev.orchestration_service.minio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/minio")
@Slf4j
public class MinioTestController
{

    private final MinioStorageService storageService;
    private final FtpToMinioService ftpToMinioService;

    public MinioTestController(MinioStorageService storageService, FtpToMinioService ftpToMinioService)
    {
        this.storageService = storageService;
        this.ftpToMinioService = ftpToMinioService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam(value = "file") MultipartFile file)

    {
        try {
            storageService.upload(file.getOriginalFilename(), file.getInputStream(), file.getContentType());
            return "Uploaded successfully";
        }
        catch (Exception e) {
            log.warn("Error : {}", e.getMessage());
            return e.getStackTrace().toString();
        }
    }

    @PostMapping("/sync")
    public String sync()
    {
        try {
            ftpToMinioService.pullAndUpload();
            return "FTP → MinIO sync completed";
        }
        catch (Exception e) {
            log.warn("Error : {}", e.getMessage());
            return e.getStackTrace().toString();
        }
    }
}

