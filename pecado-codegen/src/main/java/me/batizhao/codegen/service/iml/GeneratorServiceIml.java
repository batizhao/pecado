package me.batizhao.codegen.service.iml;

import me.batizhao.codegen.mapper.GeneratorMapper;
import me.batizhao.codegen.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Service
public class GeneratorServiceIml implements GeneratorService {

    @Autowired
    GeneratorMapper generatorMapper;

    @Override
    public List<Map<String, String>> generateCode(String name) {
        return generatorMapper.selectColumns(name);
    }
}
