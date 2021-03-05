package com.gdztyy.inca.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.KeySequence;
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
 * 订单表
 * </p>
 *
 * @author peiqy
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("B2B_ORDER_LIST")
@KeySequence("B2B_ORDER_LIST_SEQ")
public class B2bOrderList extends Model<B2bOrderList> {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private BigDecimal id;

    @TableField("B2B_ORDER_NO")
    private String b2bOrderNo;

    /**
     * 1 普通订单 2 补货单
     */
    @TableField("B2B_ORDER_TYPE")
    private String b2bOrderType;

    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    @TableField("B2B_ORDER_ID")
    private Long b2bOrderId;

    @TableField("VERSION")
    private String version;


    @TableField("SRC_DATA")
    private String srdData;

    @TableField("HINT_COUNT")
    private  Integer hintCount;
    @TableField("b2b_store_id")
    private  Long b2bStoreId;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
