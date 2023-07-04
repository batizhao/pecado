package me.batizhao.common.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batizhao
 * @date 2022/4/27
 */
@Data
public class IdName implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String code;

    private String name;

}
