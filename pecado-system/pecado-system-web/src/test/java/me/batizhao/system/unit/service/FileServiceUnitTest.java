package me.batizhao.system.unit.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

/**
 * @author batizhao
 * @date 2020/9/25
 */
public class FileServiceUnitTest extends BaseServiceUnitTest {

    @Test
    void name() {
        String name = DigestUtils.md5Hex("admin" + "IMG_0779.JPG");
        System.out.println(name);
        System.out.println(DigestUtils.md5Hex(name));
        System.out.println(DigestUtils.md5Hex(name).substring(0, 2));
        System.out.println(DigestUtils.md5Hex(name).substring(2, 4));
    }
}
