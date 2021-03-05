package com.gdztyy.inca.entity;

import java.math.BigDecimal;
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
@TableName("BMS_CREDIT_SUPPLY_INFO_DTL")
public class BmsCreditSupplyInfoDtl extends Model<BmsCreditSupplyInfoDtl> {

    private static final long serialVersionUID = 1L;

    @TableId("SUPPLY_ST_ID")
    private Long supplyStId;

    @TableField("SALERID")
    private Long salerid;

    @TableField("CREDIT")
    private BigDecimal credit;

    @TableField("DAYS")
    private Long days;


    @Override
    protected Serializable pkVal() {
        return this.supplyStId;
    }

}
