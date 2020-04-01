package me.batizhao.ims;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringCloudApplication
@EnablePecadoResourceServer
@EnableFeignClients(basePackages = "me.batizhao.system.api.feign")
public class PecadoImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoImsApplication.class, args);
    }

}
