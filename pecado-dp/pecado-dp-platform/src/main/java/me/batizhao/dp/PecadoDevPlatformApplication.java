package me.batizhao.dp;

import me.batizhao.common.datasource.annotation.EnableDynamicDataSource;
import me.batizhao.common.feign.annotation.EnablePecadoFeignClients;
import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import me.batizhao.dp.config.CodeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnablePecadoResourceServer
@EnableDynamicDataSource
@EnablePecadoFeignClients
@EnableConfigurationProperties(CodeProperties.class)
public class PecadoDevPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoDevPlatformApplication.class, args);
    }

}
