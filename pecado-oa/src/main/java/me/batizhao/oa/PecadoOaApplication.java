package me.batizhao.oa;

//import me.batizhao.common.feign.annotation.EnablePecadoFeignClients;
import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnablePecadoResourceServer
//@EnablePecadoFeignClients
public class PecadoOaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoOaApplication.class, args);
    }

}
