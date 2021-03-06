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
@TableName("PUB_GOODS_CLASS_DTL")
public class PubGoodsClassDtl extends Model<PubGoodsClassDtl> {

    private static final long serialVersionUID = 1L;

    @TableField("CLASSTYPEID")
    private Long classtypeid;

    @TableField("CLASSID")
    private Long classid;

    @TableId("GOODSID")
    private Long goodsid;


    @Override
    protected Serializable pkVal() {
        return this.goodsid;
    }

}
