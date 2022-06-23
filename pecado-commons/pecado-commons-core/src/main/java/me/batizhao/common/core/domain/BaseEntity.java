package me.batizhao.common.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 实体对象基类
 *
 * @author batizhao
 * @since 2021-04-25
 */
public class BaseEntity {

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    /** 请求参数 */
    @Schema(description = "请求参数")
    private transient Map<String, Object> params;

    /**
     * 数据权限
     */
    @Schema(description = "数据权限")
    private transient String dataPermission;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getDataPermission() {
        return dataPermission;
    }

    public void setDataPermission(String dataPermission) {
        this.dataPermission = dataPermission;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
