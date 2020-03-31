package me.batizhao.ims;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringCloudApplication
@EnablePecadoResourceServer
public class PecadoImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoImsApplication.class, args);
    }

}
