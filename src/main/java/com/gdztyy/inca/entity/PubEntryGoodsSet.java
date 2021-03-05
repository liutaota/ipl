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
@TableName("PUB_ENTRY_GOODS_SET")
public class PubEntryGoodsSet extends Model<PubEntryGoodsSet> {

    private static final long serialVersionUID = 1L;

    @TableId("SETID")
    private Long setid;

    @TableField("SETOPCODE")
    private String setopcode;

    @TableField("SETNAME")
    private String setname;

    @TableField("INPUTMANID")
    private Long inputmanid;

    @TableField("CREDATE")
    private LocalDateTime credate;

    @TableField("USESTATUS")
    private Integer usestatus;

    @TableField("ENTRYID")
    private Long entryid;

    @TableField("TYPEFLAG")
    private Integer typeflag;


    @Override
    protected Serializable pkVal() {
        return this.setid;
    }

}
