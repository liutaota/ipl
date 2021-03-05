package com.gdztyy.inca.entity;

import java.math.BigDecimal;
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
@TableName("BMS_CREDIT_TMP_1")
public class BmsCreditTmp1 extends Model<BmsCreditTmp1> {

    private static final long serialVersionUID = 1L;

    @TableId("SALERID")
    private Long salerid;

    @TableField("CUSTOMID")
    private Long customid;

    @TableField("ENTRYID")
    private Long entryid;

    @TableField("CREDIT")
    private BigDecimal credit;

    @TableField("EXEC_ORDER_ID")
    private Long execOrderId;

    @TableField("BEGINDATE")
    private LocalDateTime begindate;

    @TableField("ENDDATE")
    private LocalDateTime enddate;


    @Override
    protected Serializable pkVal() {
        return this.salerid;
    }

}
