package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 常用应用 </p>
 *
 * @author wws
 * @since 2022-05-16 10:18
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "常用应用")
@TableName("app_common")
public class AppCommon  implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Id */
    @Schema(description="id")
    private Long id;

    /** 常用应用名称 **/
    private String name;

    /** 常用应用图标 **/
    private String icon;

    /** 常用应用访问路径 **/
    private String path;

    /** 链接类型 0 内部 1 外部 **/
    private Integer linkType;

    /** 打开方式 0 非当前窗口  1 当前窗口**/
    private Integer openType;

    /** 排序 **/
    private Integer sort;

    /** 创建时间 */
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    /** 修改时间 */
    @Schema(description="修改时间")
    private LocalDateTime updateTime;
}
