package com.gdztyy.inca.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("PUB_DATAIMP_DOC")
public class PubDataimpDoc extends Model<PubDataimpDoc> {

    private static final long serialVersionUID = 1L;

    @TableId("DOCID")
    private Long docid;

    @TableField("CREDATE")
    private LocalDateTime credate;

    @TableField("INPUTMANID")
    private Long inputmanid;

    @TableField("TYPEID")
    private Integer typeid;

    @TableField("MEMO")
    private String memo;


    @Override
    protected Serializable pkVal() {
        return this.docid;
    }

}
