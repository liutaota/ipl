package com.gdztyy.api.vo;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gdztyy.inca.entity.BmsAccDef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author MP
 * @since 2021-01-08
 */
@Data

@Accessors(chain = true)
@TableName("PUB_MONTHLY_SALES")

@KeySequence(value = "sales_id_sequence")

public class PubMonthlySales extends Model<PubMonthlySales> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @ExcelIgnore
    @TableId("ID")
    private Long id;


    @ExcelProperty(index = 0,value = "客户id")
    @TableField("CUSTOMIDS")
    private Long customids;


    /*@Excel(name="日期",width=15)
    @ApiModelProperty(value = "日期")*/
    @ExcelProperty(index = 1,value = "日期")
    @TableField("TOMONTHSS")
    private String tomonthss;


    /*@Excel(name="下单次数",width=15)
    @ApiModelProperty(value = "下单次数")*/
    @ExcelProperty(index = 2,value = "下单次数")
    @TableField("SALES_LINES")
    private String salesLines;


    /*@Excel(name="中药材",width=15)
    @ApiModelProperty(value = "中药材")*/
    @ExcelProperty(index = 3,value = "中药材")
    @TableField("ZYC_LINES")
    private String zycLines;


    /*@Excel(name="处方药",width=15)
    @ApiModelProperty(value = "处方药")*/
    @ExcelProperty(index = 4,value = "处方药")
    @TableField("CFY_LINES")
    private String cfyLines;


   /* @Excel(name="非处方药",width=15)
    @ApiModelProperty(value = "非处方药")*/
    @ExcelProperty(index = 5,value = "非处方药")
    @TableField("FCFY_LINES")
    private String fcfyLines;


   /* @Excel(name="保健食品",width=15)
    @ApiModelProperty(value = "保健食品")*/
    @ExcelProperty(index = 6,value = "保健食品")
    @TableField("BJSP_LINES")
    private String bjspLines;


   /* @Excel(name="个人护理品",width=15)
    @ApiModelProperty(value = "个人护理品")*/
   @ExcelProperty(index = 7,value = "个人护理品")
    @TableField("GRFLP_LINES")
    private String grflpLines;


   /* @Excel(name="医疗器械",width=15)
    @ApiModelProperty(value = "医疗器械")*/
   @ExcelProperty(index = 8,value = "医疗器械")
    @TableField("YLQJ_LINES")
    private String ylqjLines;


    /*@Excel(name="食品饮料",width=15)
    @ApiModelProperty(value = "食品饮料")*/
    @ExcelProperty(index = 9,value = "食品饮料")
    @TableField("SPYL_LINES")
    private String spylLines;


   /* @Excel(name="总销售金额",width=15)
    @ApiModelProperty(value = "总销售金额")*/
   @ExcelProperty(index = 10,value = "总销售金额")
    @TableField("TOTAL")
    private Double total;


   /* @Excel(name="销售金额前十的商品",width=50)
    @ApiModelProperty(value = "销售金额前十的商品")*/
   @ExcelProperty(index = 11,value = "销售金额前十的商品")
    @TableField("SALES_AMOUNT")
    private String salesAmount;


   /* @Excel(name="销售数量前十的商品",width=50)
    @ApiModelProperty(value = "销售数量前十的商品")*/
   @ExcelProperty(index = 12,value = "销售数量前十的商品")
    @TableField("SALES_GOODSQTY")
    private String salesGoodsqty;
    @ExcelIgnore
    private LocalDateTime createTime;


}
