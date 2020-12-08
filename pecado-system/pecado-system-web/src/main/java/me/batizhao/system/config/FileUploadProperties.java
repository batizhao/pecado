package me.batizhao.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "pecado.upload")
public class FileUploadProperties {

    private String location = "/Users/batizhao/Documents/upload-dir";

}
