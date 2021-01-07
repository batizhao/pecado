package me.batizhao.uaa;

import me.batizhao.common.feign.annotation.EnablePecadoFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringCloudApplication
@EnablePecadoFeignClients
public class PecadoUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoUaaApplication.class, args);
    }

}
