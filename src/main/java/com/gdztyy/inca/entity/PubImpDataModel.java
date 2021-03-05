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
@TableName("PUB_IMP_DATA_MODEL")
public class PubImpDataModel extends Model<PubImpDataModel> {

    private static final long serialVersionUID = 1L;

    @TableId("MODELID")
    private Long modelid;

    @TableField("MODELTYPE")
    private Integer modeltype;

    @TableField("MEMO")
    private String memo;


    @Override
    protected Serializable pkVal() {
        return this.modelid;
    }

}
