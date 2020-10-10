package me.batizhao.codegen.service;

import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
public interface GeneratorService {

    List<Map<String, String>> generateCode(String name);

}
