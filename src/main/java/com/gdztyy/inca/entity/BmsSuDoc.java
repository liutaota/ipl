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
@TableName("BMS_SU_DOC")
public class BmsSuDoc extends Model<BmsSuDoc> {

    private static final long serialVersionUID = 1L;

    @TableId("SUDOCID")
    private Long sudocid;

    @TableField("CREDATE")
    private LocalDateTime credate;

    @TableField("SUPPLYID")
    private Long supplyid;

    @TableField("SUPPLYNAME")
    private String supplyname;

    @TableField("TOTALCOST")
    private BigDecimal totalcost;

    @TableField("TOTAL")
    private BigDecimal total;

    @TableField("DTL_LINES")
    private Integer dtlLines;

    @TableField("INPUTMANID")
    private Long inputmanid;

    @TableField("ARRIVEDATE")
    private LocalDateTime arrivedate;

    @TableField("DELIVERDATE")
    private LocalDateTime deliverdate;

    @TableField("TRANSDOCNO")
    private String transdocno;

    @TableField("MEMO")
    private String memo;

    @TableField("SUTYPEID")
    private Integer sutypeid;

    @TableField("INITFLAG")
    private Integer initflag;

    @TableField("ENTRYID")
    private Long entryid;

    @TableField("TRANMETHOD")
    private Integer tranmethod;

    @TableField("MIDPOSID")
    private Long midposid;

    @TableField("TARGETPOSID")
    private Long targetposid;

    @TableField("URGENFLAG")
    private Integer urgenflag;

    @TableField("TRANPRIORITY")
    private Integer tranpriority;

    @TableField("TARGETDATE")
    private LocalDateTime targetdate;

    @TableField("DELIVERMETHOD")
    private Integer delivermethod;

    @TableField("FMID")
    private Long fmid;

    @TableField("EXCHANGE")
    private BigDecimal exchange;

    @TableField("PRINTCOUNT")
    private Long printcount;

    @TableField("PRINTMANID")
    private Long printmanid;

    @TableField("PRINTDATE")
    private LocalDateTime printdate;

    @TableField("CONFIRMMANID")
    private Long confirmmanid;

    @TableField("CONFIRMDATE")
    private LocalDateTime confirmdate;

    @TableField("USESTATUS")
    private Integer usestatus;

    @TableField("PLACEPOINTID")
    private Long placepointid;

    @TableField("WFUSESTATUS")
    private Integer wfusestatus;

    @TableField("WFPROCESS")
    private Integer wfprocess;

    @TableField("WFMEMO")
    private String wfmemo;

    @TableField("AGENTID")
    private Long agentid;

    @TableField("ADJDATE")
    private LocalDateTime adjdate;

    @TableField("PLACESUPPLYID")
    private Long placesupplyid;

    @TableField("COMEFROM")
    private Integer comefrom;

    @TableField("NOWMSFLAG")
    private Integer nowmsflag;

    @TableField("GSPUNCONFIRMFLAG")
    private Integer gspunconfirmflag;

    @TableField("WMSDESCR")
    private String wmsdescr;

    @TableField("WMSDATE")
    private LocalDateTime wmsdate;

    @TableField("CMSFLAG")
    private Integer cmsflag;

    @TableField("CONTACTID")
    private Long contactid;

    @TableField("TAXMONEY")
    private BigDecimal taxmoney;

    @TableField("TWOINVOICEINFLAG")
    private Integer twoinvoiceinflag;

    @TableField("BMS_SU_DOC")
    private Integer bmsSuDoc;

    @TableField("ZX_INITSATUS")
    private Integer zxInitsatus;


    @Override
    protected Serializable pkVal() {
        return this.sudocid;
    }

}
