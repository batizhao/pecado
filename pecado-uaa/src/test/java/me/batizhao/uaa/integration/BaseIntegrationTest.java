package me.batizhao.uaa.integration;

import me.batizhao.common.core.exception.WebExceptionHandler;
import me.batizhao.uaa.PecadoUaaApplication;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author batizhao
 * @since 2020-03-31
 **/
//@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'integration'}", loadContext = true)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoUaaApplication.class)
@AutoConfigureMockMvc
@Import(WebExceptionHandler.class)
@Tag("integration")
public abstract class BaseIntegrationTest {

    @Autowired
    MockMvc mvc;

}
