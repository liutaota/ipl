package com.gdztyy.inca.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdztyy.api.vo.BmsCreditBillVo;
import com.gdztyy.api.vo.CustomerVo;
import com.gdztyy.api.vo.IncaGoodsCustomVo;
import com.gdztyy.inca.entity.BmsCreditBillDoc;
import com.gdztyy.inca.entity.BmsCreditBillDtl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author peiqy
 * @since 2020-08-18
 */
public interface BmsCreditBillDtlMapper extends BaseMapper<BmsCreditBillDtl> {

    @Select("select bms_cert_dtl_tmp_trans_seq.nextval TraId from dual")
    Long queryByTraId();

    @Select("select billid,owemoney from bms_credit_bill_doc where 1=1  and customid = #{customId} and salerid = #{salerId} and salerdeptid = #{salesdeptId} and entryid = 1 and rownum=1")
    BmsCreditBillVo queryByid(@Param("customId") long customId, @Param("salerId") Long salerId, @Param("salesdeptId") Long salesdeptId);

    @Update("update bms_credit_bill_doc set owemoney =#{owemoney} where billid = #{billid}")
    void updateByBillid(Long billid, Double owemoney);

    @Select("select entryid, customid,goodsid,lastsadtlid,lastsaprice from bms_goods_saprice_custom_ref where entryid=1 and customid=#{customId} and goodsid=#{goodsId}")
    IncaGoodsCustomVo queryByCust(@Param("customId") Long customId, @Param("goodsId") Long goodsId);

    @Update("update bms_goods_saprice_custom_ref set lastsadtlid=#{lastsadtlid},lastsaprice=#{lastsaprice} where entryid=1 and customid=#{customId} and goodsid=#{goodsId}")
    void updateByPrice(@Param("lastsadtlid") Long lastsadtlid, @Param("lastsaprice") Double lastsaprice, @Param("customId") Long customId, @Param("goodsId") Long goodsId);

}
