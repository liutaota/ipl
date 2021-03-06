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
@TableName("PUB_EMP_SUPPLY_GOODS_AP")
public class PubEmpSupplyGoodsAp extends Model<PubEmpSupplyGoodsAp> {

    private static final long serialVersionUID = 1L;

    @TableId("SEQID")
    private Long seqid;

    @TableField("EMPLOYEEID")
    private Long employeeid;

    @TableField("SUPPLYID")
    private Long supplyid;

    @TableField("AGENTID")
    private Long agentid;

    @TableField("GOODSID")
    private Long goodsid;

    @TableField("MEMO")
    private String memo;


    @Override
    protected Serializable pkVal() {
        return this.seqid;
    }

}
