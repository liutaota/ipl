package com.gdztyy.inca.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("BMS_SU_PLAN_EXPTMP")
public class BmsSuPlanExptmp extends Model<BmsSuPlanExptmp> {

    private static final long serialVersionUID = 1L;

    @TableField("HEADER")
    private String header;

    @TableField("FOOTER")
    private String footer;

    @TableId("SEQID")
    private Long seqid;


    @Override
    protected Serializable pkVal() {
        return this.seqid;
    }

}
