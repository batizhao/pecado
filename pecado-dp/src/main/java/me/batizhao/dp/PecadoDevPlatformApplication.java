package me.batizhao.dp;

import me.batizhao.common.datasource.annotation.EnableDynamicDataSource;
import me.batizhao.common.feign.annotation.EnablePecadoFeignClients;
import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringCloudApplication
@EnablePecadoResourceServer
@EnableDynamicDataSource
@EnablePecadoFeignClients
public class PecadoDevPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoDevPlatformApplication.class, args);
    }

}
