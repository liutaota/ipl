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
@TableName("BMS_SA_RELATION")
public class BmsSaRelation extends Model<BmsSaRelation> {

    private static final long serialVersionUID = 1L;

    @TableId("OLDSALESDTLID")
    private Long oldsalesdtlid;

    @TableField("TOSALESDTLID")
    private Long tosalesdtlid;


    @Override
    protected Serializable pkVal() {
        return this.oldsalesdtlid;
    }

}
