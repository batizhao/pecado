package me.batizhao.system;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author batizhao
 * @since 2020/3/28
 */
@SpringCloudApplication
@EnablePecadoResourceServer
@EnableFeignClients(basePackages = "me.batizhao.ims.api.feign")
public class PecadoSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoSystemApplication.class, args);
    }

}
