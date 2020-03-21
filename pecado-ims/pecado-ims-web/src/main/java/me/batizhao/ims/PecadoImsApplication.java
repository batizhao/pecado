package me.batizhao.ims;

import me.batizhao.common.security.annotation.EnablePecadoResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@SpringBootApplication
@EnablePecadoResourceServer
public class PecadoImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PecadoImsApplication.class, args);
    }

}
