package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 组件模板 </p>
 *
 * @author wws
 * @since 2022-05-13 10:12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "组件模板")
@TableName("page_component")
public class PageComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * 缩略图
     */
    @Schema(description="缩略图")
    private String thumbnail;

    /**
     * 组件类型 C、组件 P、页面
     */
    @Schema(description="组件类型 C、组件 P、页面")
    private String type;

    /**
     * 组件所属页面类型：首页页面模型index、主页页面模型main、列表页面模型list、表单页面模型form、应用表单组件appForm
     */
    @Schema(description="首页页面模型index、主页页面模型main、列表页面模型list、表单页面模型form、应用表单组件appForm")
    private String model;

    /**
     * 列元数据
     */
    @Schema(description="列元数据")
    private String metadata;

    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description="修改时间")
    private LocalDateTime updateTime;
}
