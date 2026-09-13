package com.dev.orchestration_service.ftp;

import com.dev.orchestration_service.config.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FtpService
{
    private final FtpProperties ftpProperties;

    public FtpService(FtpProperties ftpProperties) {this.ftpProperties = ftpProperties;}


    // For testing
    public void fetchFiles()
            throws IOException
    {
        FTPClient ftp = new FTPClient();
        ftp.connect(ftpProperties.getHost(), ftpProperties.getPort());
        ftp.login(ftpProperties.getUsername(), ftpProperties.getPassword());
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        ftp.changeWorkingDirectory("/test");
        FTPFile[] files = ftp.listFiles();

        for (FTPFile file : files) {
            if (!file.isFile()) {
                continue;
            }

            try (InputStream is = ftp.retrieveFileStream(file.getName())) {
                log.info("Fetched file: " + file.getName());
            }
            ftp.completePendingCommand();
        }
        ftp.logout();
        ftp.disconnect();
    }
}

