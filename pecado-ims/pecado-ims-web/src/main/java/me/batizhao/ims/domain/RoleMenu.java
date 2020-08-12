package me.batizhao.ims.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RoleMenu {

    private Long roleId;

    private Long menuId;

    private String path;
    private String roleCode;

    public RoleMenu(String path, String roleCode) {
        this.path = path;
        this.roleCode = roleCode;
    }
}
