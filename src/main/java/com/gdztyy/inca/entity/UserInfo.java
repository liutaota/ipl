package com.gdztyy.inca.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author peiqy
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("USER_INFO")
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Long id;

    @TableField("USER_NAME")
    private String userName;

    @TableField("NAME")
    private String name;

    @TableField("PASSWORD")
    private String password;

    @TableField("CUSTOM_ID")
    private Long customId;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("IS_DEL")
    private Integer isDel;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
