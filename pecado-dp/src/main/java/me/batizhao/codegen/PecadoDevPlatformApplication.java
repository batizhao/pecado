package me.batizhao.codegen;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringCloudApplication
@EnablePecadoResourceServer
public class PecadoDevPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoDevPlatformApplication.class, args);
    }

}
