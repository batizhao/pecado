package me.batizhao.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 系统配置 实体对象
 *
 * @author batizhao 
 * @since 2022-05-16
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "系统配置")
@TableName(value = "config")
public class Config extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * $column.comment
     */
    @Schema(description="$column.comment")
    private Integer id;

    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * 编码
     */
    @Schema(description="编码")
    private String code;

    /**
     * 值
     */
    @Schema(description="值")
    private String value;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 描述
     */
    @Schema(description="描述")
    private String description;


}
