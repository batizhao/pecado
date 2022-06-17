package me.batizhao.ims.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.common.core.domain.TreeNode;
import me.batizhao.ims.api.vo.MetaVO;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "菜单")
public class Menu extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    public Menu(Integer id, Integer pid) {
        this.id = id;
        this.pid = pid;
    }

    @Schema(description = "路径", example = "/user/common")
    private String path;

    @Schema(description = "菜单名", example = "权限管理")
    @NotBlank(message = "name is not blank")
    private String name;

    @Schema(description = "权限名", example = "ims_root")
    private String permission;

    @Schema(description = "权限说明", example = "This is admin permission")
    private String description;

    @Schema(description = "图标", example = "icon-web")
    private String icon;

    @Schema(description = "类型（M菜单 B按钮）", example = "M")
    private String type;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "页面模型编码")
    private String pageModelCode;

    @Schema(description = "应用页面编码")
    private String appPageCode;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 路由元数据
     */
    @Schema(description="路由元数据")
    private transient MetaVO meta;

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private Long appId;

    /**
     * 菜单范围
     */
    @Schema(description="菜单范围")
    private String scope;

}
