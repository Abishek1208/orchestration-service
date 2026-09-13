package com.dev.orchestration_service.minio;

import com.dev.orchestration_service.config.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class FtpToMinioService
{

    private final MinioStorageService minioStorageService;
    private final FtpProperties ftpProperties;

    public FtpToMinioService(MinioStorageService minioStorageService, FtpProperties ftpProperties)
    {
        this.minioStorageService = minioStorageService;
        this.ftpProperties = ftpProperties;
    }

    public void pullAndUpload()
    {
        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(ftpProperties.getHost(), ftpProperties.getPort());
            ftp.login(ftpProperties.getUsername(), ftpProperties.getPassword());
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            FTPFile[] files = ftp.listFiles(ftpProperties.getRemoteDir());

            for (FTPFile file : files) {
                if (!file.isFile()) {
                    continue;
                }

                String fileName = file.getName();
                String ftpPath = ftpProperties.getRemoteDir() + "/" + fileName;

                log.info("Processing: {}", fileName);

                try (InputStream is = ftp.retrieveFileStream(ftpPath)) {

                    // 1️⃣ Upload to MinIO
                    minioStorageService.upload(fileName, is, detectContentType(fileName));
                }

                ftp.completePendingCommand();
                log.info("Uploaded & moved: {}", fileName);
            }

            ftp.logout();
            ftp.disconnect();
        }
        catch (Exception e) {
            throw new RuntimeException("FTP → MinIO failed", e);
        }
    }

    private String detectContentType(String fileName)
    {
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }
}

