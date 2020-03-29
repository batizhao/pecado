package me.batizhao.ims.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author batizhao
 * @since 2020-02-07
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public abstract class BaseServiceUnitTest {
}
