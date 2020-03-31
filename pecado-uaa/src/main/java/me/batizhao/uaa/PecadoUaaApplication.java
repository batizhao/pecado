package me.batizhao.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = "me.batizhao.ims.api.feign")
public class PecadoUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoUaaApplication.class, args);
    }

}
