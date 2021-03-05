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
@TableName("PUB_IMP_ENTRY_CUSTOM")
public class PubImpEntryCustom extends Model<PubImpEntryCustom> {

    private static final long serialVersionUID = 1L;

    @TableField("DOCID")
    private Long docid;

    @TableId("DTLID")
    private Long dtlid;

    @TableField("IMPSTATUS")
    private Integer impstatus;

    @TableField("ERRORMESSAGE")
    private String errormessage;

    @TableField("NEWID")
    private Long newid;

    @TableField("CUSTOMNAME")
    private String customname;

    @TableField("ENTRYNAME")
    private String entryname;

    @TableField("FIRSTAPPROVEDATE")
    private String firstapprovedate;

    @TableField("LOWPRICEFLAG")
    private String lowpriceflag;

    @TableField("SETTLETYPENAME")
    private String settletypename;

    @TableField("TRANPRIORITY")
    private String tranpriority;

    @TableField("DELIVERMETHODNAME")
    private String delivermethodname;

    @TableField("DEFAULTTRANMETHODNAME")
    private String defaulttranmethodname;

    @TableField("DEFAULTINVOICETYPENAME")
    private String defaultinvoicetypename;

    @TableField("REQPRINTQUFLAG")
    private String reqprintquflag;

    @TableField("FINANCENO")
    private String financeno;

    @TableField("PRICENAME")
    private String pricename;

    @TableField("ENTRYMEMO")
    private String entrymemo;

    @TableField("ZXCOLUMN01")
    private String zxcolumn01;

    @TableField("ZXCOLUMN02")
    private String zxcolumn02;

    @TableField("ZXCOLUMN03")
    private String zxcolumn03;

    @TableField("ZXCOLUMN04")
    private String zxcolumn04;

    @TableField("ZXCOLUMN05")
    private String zxcolumn05;

    @TableField("ZXCOLUMN06")
    private String zxcolumn06;

    @TableField("ZXCOLUMN07")
    private String zxcolumn07;

    @TableField("ZXCOLUMN08")
    private String zxcolumn08;

    @TableField("ZXCOLUMN09")
    private String zxcolumn09;

    @TableField("ZXCOLUMN10")
    private String zxcolumn10;

    @TableField("ZXCOLUMN11")
    private String zxcolumn11;

    @TableField("ZXCOLUMN12")
    private String zxcolumn12;

    @TableField("ZXCOLUMN13")
    private String zxcolumn13;

    @TableField("ZXCOLUMN14")
    private String zxcolumn14;

    @TableField("ZXCOLUMN15")
    private String zxcolumn15;

    @TableField("ZXCOLUMN16")
    private String zxcolumn16;

    @TableField("ZXCOLUMN17")
    private String zxcolumn17;

    @TableField("ZXCOLUMN18")
    private String zxcolumn18;

    @TableField("ZXCOLUMN19")
    private String zxcolumn19;

    @TableField("ZXCOLUMN20")
    private String zxcolumn20;

    @TableField("CUSTOMID")
    private Long customid;

    @TableField("ENTRYID")
    private Long entryid;

    @TableField("SETTLETYPEID")
    private Integer settletypeid;

    @TableField("DELIVERMETHOD")
    private Integer delivermethod;

    @TableField("DEFAULTTRANMETHOD")
    private Integer defaulttranmethod;

    @TableField("PRICEID")
    private Long priceid;

    @TableField("FMNAME")
    private String fmname;

    @TableField("ISMUSTAGENT")
    private String ismustagent;

    @TableField("ISMUSTCONTACT")
    private String ismustcontact;

    @TableField("TWOINVFLAG")
    private String twoinvflag;

    @TableField("TWOINVMETHOD")
    private String twoinvmethod;

    @TableField("TWOINVMETHODID")
    private Integer twoinvmethodid;


    @Override
    protected Serializable pkVal() {
        return this.dtlid;
    }

}
