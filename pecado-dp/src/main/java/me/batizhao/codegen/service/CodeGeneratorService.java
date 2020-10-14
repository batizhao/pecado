package me.batizhao.codegen.service;

import me.batizhao.codegen.domain.GenConfig;

/**
 * @author batizhao
 * @date 2020/10/10
 */
public interface CodeGeneratorService {

    byte[] generateCode(GenConfig genConfig);

}
