package me.batizhao.dp.service;

import me.batizhao.dp.domain.GenConfig;

/**
 * @author batizhao
 * @date 2020/10/10
 */
public interface CodeGeneratorService {

    byte[] generateCode(GenConfig genConfig);

}
