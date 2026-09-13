package com.dev.orchestration_service.ftp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FtpController
{
    private final FtpService ftpService;

    public FtpController(FtpService ftpService) {this.ftpService = ftpService;}

    @GetMapping("/pull")
    public String pull()
            throws IOException
    {
        ftpService.fetchFiles();
        return "FTP fetch completed";
    }
}
