package me.batizhao.system.unit.util;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.component.PecadoUser;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.system.util.FileNameAndPathUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

/**
 * @author batizhao
 * @date 2020/9/28
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Tag("unit")
@Slf4j
public class FileNameAndPathUtilsUnitTest {

    @Test
    public void testFileNameEncode() {
        PecadoUser pecadoUser = new PecadoUser(1L, Collections.singletonList("2"), Collections.singletonList("1"), "zhangsan", "N_A", true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        try (MockedStatic<SecurityUtils> mockStatic = mockStatic(SecurityUtils.class)) {
            mockStatic.when(SecurityUtils::getUser).thenReturn(pecadoUser);
            SecurityUtils.getUser();
            mockStatic.verify(times(1), SecurityUtils::getUser);

            String result = FileNameAndPathUtils.fileNameEncode("abc.txt");

            log.info("result: {}", result);

            assertThat(result, equalTo("03539a505050c9b856fab68d0fcc7aaf.txt"));
        }
    }

    @Test
    public void testPathEncode() {
        String result = FileNameAndPathUtils.pathEncode("03539a505050c9b856fab68d0fcc7aaf.txt");

        log.info("result: {}", result);

        assertThat(result, equalTo("7b/f0/03539a505050c9b856fab68d0fcc7aaf.txt"));
    }
}
