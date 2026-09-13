package com.dev.orchestration_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ftp")
@Data
public class FtpProperties
{
    private String host;
    private int port;
    private String username;
    private String password;
    private String remoteDir;
}

