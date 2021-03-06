package com.gdztyy.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gdztyy.api.service.CustomService;
import com.gdztyy.api.service.GoodsService;
import com.gdztyy.api.service.OrderService;
import com.gdztyy.api.vo.*;
import com.gdztyy.inca.entity.*;
import com.gdztyy.inca.mapper.*;
import com.gdztyy.util.AssertExt;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    GpcsPlacepointMapper gpcsPlacepointMapper;
    @Resource
    CustomService customService;
    @Resource
    GoodsService goodsService;
    @Resource
    BmsPresOutDocMapper bmsPresOutDocMapper;
    @Resource
    BmsSaDocMapper bmsSaDocMapper;
    @Resource
    BmsSaDtlMapper bmsSaDtlMapper;
    @Resource
    BmsPresOutDtlMapper bmsPresOutDtlMapper;
    @Resource
    BmsStIoDocTmpMapper bmsStIoDocTmpMapper;
    @Resource
    BmsStIoDtlTmpMapper bmsStIoDtlTmpMapper;
    @Resource
    GpcsPlacesupplyMapper gpcsPlacesupplyMapper;
    @Resource
    GpcsPlacesupplydtlMapper gpcsPlacesupplydtlMapper;
    @Resource
    GpcsPlacesupplydtlStMapper gpcsPlacesupplydtlStMapper;
    @Resource
    GpcsPlacesupplydtlStdtlMapper gpcsPlacesupplydtlStdtlMapper;
    @Resource
    RedissonClient redissonClient;

    @Resource
    BmsTrDocMapper incaBmstrdocMapper;
    @Resource
    BmsTrDtlMapper incaBmstrdtlMapper;
    @Resource
    BmsSaConDocMapper bmsSaConDocMapper;
    @Resource
    BmsSaConDtlMapper bmsSaConDtlMapper;
    @Resource
    BmsSaContodocMapper bmsSaConToDocMapper;
    @Resource
    BmsTrDocMapper bmsTrDocMapper;
    @Resource
    BmsTrDtlMapper bmsTrDtlMapper;
    @Resource
    BmsTrBackInsertMapper incaTrBackInsertMapper;
    @Resource
    BmsCertDtlTmpMapper bmsCertDtlTmpMapper;

    @Resource
    BmsCreditBillDtlMapper bmsCreditBillDtlMapper;

    @Resource
    BmsStDefMapper bmsStDefMapper;

    @Resource
    B2bOrderListMapper b2bOrderListMapper;
    @Resource
    BmsPresOutDocMapper b2bGiftDocComfirmMapper;

    @Resource
    BmsStQtyLstMapper bmsStQtyLstMapper;

    @Resource
    BmsStIoDocMapper bmsStIoDocMapper;
    @Resource
    BmsStIoDtlMapper bmsStIoDtlMapper;

    @Resource
    PubEmployeeMapper pubEmployeeMapper;

    @Resource
    BmsTrPickDocMapper bmsTrPickDocMapper;

    @Resource
    BmsSaDtlTmpMapper bmsSaDtlTmpMaper;
    private List<Integer> platformList = new ArrayList<Integer>(){{
        this.add(21);
    }};

    @Override
    public OrderResultDocVo genOrder(com.gdztyy.api.vo.OrderInfoDocVo orderInfoDocVo) {
        AssertExt.notNull(orderInfoDocVo, "??????????????????");
        AssertExt.notEmpty(orderInfoDocVo.getOrderInfoDtlList(), "???????????????????????????");

        com.gdztyy.api.vo.OrderResultDocVo resultVo = null;


        String orderNo = orderInfoDocVo.getOrderNo();
        Long orderId = orderInfoDocVo.getOrderId();
        //Long customId = orderInfoDocVo.getCustomId();
        Integer entryId = orderInfoDocVo.getEntryId();
        Long b2bStoreId=orderInfoDocVo.getStoreId();
        AssertExt.notNull(b2bStoreId, "StoreId??????????????????");
        BmsTrPosDef  bmsTrPosDef= customService.getCustomIdByStoreId(entryId,b2bStoreId);
        Long customId=bmsTrPosDef.getCompanyid();
        orderInfoDocVo.setCustomId(customId);
        ResultVo result1 = customService.valid(customId, entryId);

        ArrayList<Long> goodsIdList = new ArrayList<>();
        orderInfoDocVo.getOrderInfoDtlList().forEach(item -> {
            goodsIdList.add(item.getGoodsId());
        });
        result1 = ResultVo.failAppend(result1, goodsService.valid(customId, entryId, goodsIdList));

        if (result1.isError()) {
            return OrderResultDocVo.validReturn(result1, orderNo, customId);
        }





        RLock lock = redissonClient.getLock("order_gen");
        /**
         * ???????????????
         */
        lock.lock(6, TimeUnit.MINUTES);

        try {

            log.info("??????????????????---------????????????----------");
           // Thread.sleep(5000);
            QueryWrapper<B2bOrderList> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(B2bOrderList::getB2bOrderId, orderId);
            queryWrapper.lambda().eq(B2bOrderList::getB2bOrderType, orderInfoDocVo.getOrderType());
            queryWrapper.lambda().eq(B2bOrderList::getVersion, orderInfoDocVo.getVersion());
            if (b2bOrderListMapper.selectCount(queryWrapper) == 0) {

                B2bOrderList b2bOrderList = new B2bOrderList();

                b2bOrderList.setB2bOrderNo(orderNo);
                b2bOrderList.setB2bOrderId(orderId);

                b2bOrderList.setB2bOrderType(orderInfoDocVo.getOrderType());
                b2bOrderList.setVersion(orderInfoDocVo.getVersion());
                b2bOrderList.setCreateDate(LocalDateTime.now());
               // b2bOrderList.setb2bOrderId(b2bStoreId);
                b2bOrderList.setSrdData(JSON.toJSONString(orderInfoDocVo, SerializerFeature.PrettyFormat,
                        SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.UseSingleQuotes));
                b2bOrderList.setB2bStoreId(b2bStoreId);

                b2bOrderList.setHintCount(1);
                b2bOrderListMapper.insert(b2bOrderList);


                /**
                 * ????????????????????????
                 */
                OrderInfoDocVo giftOrder = decomposeOrderDetails(orderInfoDocVo, true);

                /**
                 * ????????????????????????
                 */
                OrderInfoDocVo normalOrder = decomposeOrderDetails(orderInfoDocVo, false);

                /*if (ObjectUtil.isNotNull(giftOrder)) {
                    resultVo = OrderResultDocVo.undoneAppend(resultVo, genOrderGift(giftOrder), orderNo, customId);
                    confirmGiftOrder(orderNo,entryId,1,2,platformList);
                }*/

                if (customService.isPsCustom(orderInfoDocVo.getCustomId(), orderInfoDocVo.getEntryId())) {
                    resultVo = OrderResultDocVo.undoneAppend(resultVo, genOrderPs(normalOrder), orderNo, customId);
                    //confirmPsOrder(orderNo,entryId,1,2,platformList);
                } else {
                    resultVo = OrderResultDocVo.undoneAppend(resultVo, genOrderSa(normalOrder), orderNo, customId);

                    //  confirmSaOrder(orderNo,entryId,2,1,platformList);
                }


            } else {
                b2bOrderListMapper.add1HintCount(queryWrapper);
                AssertExt.fail("????????????????????????!!!!");
            }


        }catch (Throwable e) {
                log.error("?????????????????????!", e);
                AssertExt.fail("?????????????????????:" + e.getMessage(), e);

        } finally {
            log.info("??????????????????---------??????----------");
            lock.unlock();

        }
        return OrderResultDocVo.validReturn(resultVo, orderNo, customId);

    }

    /**
     * ????????????
     * @param orderInfoDocVo
     * @return
     */
    @Override
    public OrderResultDocVo receiptOrder(OrderInfoDocVo orderInfoDocVo) {
        AssertExt.notNull(orderInfoDocVo, "??????????????????");

        OrderResultDocVo resultVo = null;

        String orderId = orderInfoDocVo.getOrderNo();
        AssertExt.notNull(orderId, "????????????id??????");

        Long customId = orderInfoDocVo.getCustomId();
        AssertExt.notNull(customId, "??????id??????");

        Integer entryId = orderInfoDocVo.getEntryId();
        AssertExt.notNull(entryId, "????????????id??????");

        if(customService.isPsCustom(orderInfoDocVo.getCustomId(),orderInfoDocVo.getEntryId())){
            /**
             * ????????????????????????
             */
            resultVo = receiptOrderPs(orderInfoDocVo);

        }
        return OrderResultDocVo.validReturn(resultVo,orderId,customId);
    }

    private OrderResultDocVo receiptOrderPs(OrderInfoDocVo orderInfoDocVo) {
        OrderResultDocVo resultVo = null;
        String orderId = orderInfoDocVo.getOrderNo();
        Long customId = orderInfoDocVo.getCustomId();
        Integer entryId = orderInfoDocVo.getEntryId();

        /**
         * ???????????????????????????????????????????????????
         */
        /**
         * ?????????????????????????????????
         */
        for(OrderInfoDtlVo orderInfoDtlVo:orderInfoDocVo.getOrderInfoDtlList()){
            Long goodsid=orderInfoDtlVo.getGoodsId();
            AssertExt.notNull(goodsid,"?????????????????????id?????????");

            Long srcErpOrderId=orderInfoDtlVo.getSrcErpOrderId();
            AssertExt.notNull(srcErpOrderId,"???????????????????????????id?????????");

            Long srcErpOrderDtlId=orderInfoDtlVo.getSrcErpOrderDtlId();
            AssertExt.notNull(srcErpOrderDtlId,"???????????????????????????id?????????");
        }

        /**
         * ???????????????????????????????????????????????????
         */
        for(OrderInfoDtlVo orderInfoDtlVo:orderInfoDocVo.getOrderInfoDtlList()){
            Long goodsid=orderInfoDtlVo.getGoodsId();
            Long srcErpOrderId=orderInfoDtlVo.getSrcErpOrderId();
            Long srcErpOrderDtlId=orderInfoDtlVo.getSrcErpOrderDtlId();

            /**
             * ???????????????????????????bms_sa_doc???
             */
            BmsSaDoc bmsSaDoc=this.bmsSaDocMapper.selectById(srcErpOrderId);
            if(ObjectUtils.isEmpty(bmsSaDoc)){
                return OrderResultDocVo.failErrorMessageAppend(resultVo,"??????????????????("+srcErpOrderId+")????????????",orderId,customId);
            }
            /**
             * ???????????????????????????bms_sa_dtl???
             */
            BmsSaDtl bmsSaDtl=this.bmsSaDtlMapper.selectById(srcErpOrderDtlId);
            if(ObjectUtils.isEmpty(bmsSaDtl)){
                return OrderResultDocVo.failErrorMessageAppend(resultVo,"?????????????????????"+srcErpOrderDtlId+"?????????",orderId,customId);
            }
            /**
             * ?????????????????????gpcs_placesupply???
             */
            GpcsPlacesupply gpcsPlacesupply=this.gpcsPlacesupplyMapper.selectById(bmsSaDoc.getPlacesupplyid());
            if(ObjectUtils.isEmpty(gpcsPlacesupply)){
                return OrderResultDocVo.failErrorMessageAppend(resultVo,"??????????????????"+bmsSaDoc.getPlacesupplyid()+"?????????",orderId,customId);
            }
            /**
             * ???????????????????????????gpcs_placepoint???
             */
            GpcsPlacepoint gpcsPlacepoint=this.gpcsPlacepointMapper.selectById(customId);
            if(ObjectUtils.isEmpty(gpcsPlacepoint)){
                return OrderResultDocVo.failErrorMessageAppend(resultVo,"???????????????"+customId+"?????????",orderId,customId);
            }
            /**
             * ???????????????????????????gpcs_placesupplydtl???
             */
            GpcsPlacesupplydtl gpcsPlacesupplydtl=this.getGpcsPlacesupplydtl(gpcsPlacesupply.getPlacesupplyid(),bmsSaDtl.getGoodsid(),orderInfoDtlVo.getBatchId());
            if(ObjectUtils.isEmpty(gpcsPlacesupplydtl)){
                return OrderResultDocVo.failErrorMessageAppend(resultVo,"???????????????????????????",orderId,customId);
            }
            /**
             * ???????????????????????????gpcs_placesupply_tiny???
             */
            Long gpcsPlacesupplyTiny=this.getGpcsPlacesupplyTinyId(gpcsPlacesupplydtl.getPlacesupplydtlid());
            if(ObjectUtils.isEmpty(gpcsPlacesupplyTiny)){
                return OrderResultDocVo.failErrorMessageAppend(resultVo,"???????????????????????????",orderId,customId);
            }

            /**
             * 1.????????????????????????
             */
            /**
             * 2.????????????????????????????????????????????????
             */
            /**
             * 3.????????????????????????
             */
        }

        /**
         * ????????????????????????
         */
        return OrderResultDocVo.validReturn(resultVo,orderId,customId);
    }

    private Long getGpcsPlacesupplyTinyId(Long placesupplydtlid) {
        return null;
    }

    private GpcsPlacesupplydtl getGpcsPlacesupplydtl(Long placesupplyid, Long goodsid, Long batchId) {
        return null;
    }

    @Override
    public List<String> selectOrderNotCallback(Integer zxBhFlag, Integer b2bNotWriteBack, Integer useStatus) {
        return bmsSaDocMapper.selectNotCallbackBy(zxBhFlag, b2bNotWriteBack, useStatus);
    }



    @Override
    public void updateCallbackStatus(List<String> orderIds, Integer i) {
        bmsSaDocMapper.updateCallbackStatus(orderIds, i);
    }

    @Override
    public Integer getCountBy(Long saleId, String orderId) {
        return bmsSaDocMapper.selectCountBy(saleId, orderId);
    }


    private OrderResultDocVo genOrderGift(OrderInfoDocVo giftOrder) {

        OrderResultDocVo orderResultDocVo = null;
        Long orderId = giftOrder.getOrderId();
        String orderNo = giftOrder.getOrderNo();
        Long customId = giftOrder.getCustomId();
        Integer entryId = giftOrder.getEntryId();

        PubCustomToSaler saler = customService.getSalerByCustomId(customId, entryId);

        PubCustomer customer = customService.getById(customId);
        /**
         * ????????????
         */
        BmsPresOutDoc bmsPresOutDoc = new BmsPresOutDoc();


        bmsPresOutDoc.setMemo((ObjectUtil.isNotNull(giftOrder.getRemark()) ? "B2B??????,???" + giftOrder.getRemark() : "") + "B2B????????????" + giftOrder.getOrderNo());
        bmsPresOutDoc.setB2bOrderNo(orderNo);
        bmsPresOutDoc.setB2bOrderId(orderId);
        bmsPresOutDoc.setB2bAmountTotal(giftOrder.getAmountTotal());
        bmsPresOutDoc.setB2bAmountPay(giftOrder.getAmountPay());
        bmsPresOutDoc.setB2bAmountDelivery(giftOrder.getAmountDelivery());
        bmsPresOutDoc.setB2bAmountDiscount(giftOrder.getAmountDiscount());

        bmsPresOutDoc.setCustomid(customId);
        bmsPresOutDoc.setCustomname(customer.getCustomname());
        bmsPresOutDoc.setEntryid(entryId);
        bmsPresOutDoc.setPolicytype(1);
        bmsPresOutDoc.setComefrom(1);
        bmsPresOutDoc.setPrintflag(0);
        bmsPresOutDoc.setEntryid(entryId);
        /**
         * 11 ?????? b2b??????
         */
        bmsPresOutDoc.setZxBhFlag(21);
        //????????????
        bmsPresOutDoc.setCredate(LocalDateTime.now());

        bmsPresOutDoc.setUsestatus(2);

        bmsPresOutDoc.setInputmanid(saler.getSalerid());

        bmsPresOutDocMapper.insert(bmsPresOutDoc);
        Integer dtlLines = 0;
        for (OrderInfoDtlVo orderInfoDtlVo : giftOrder.getOrderInfoDtlList()) {
            OrderResultDocVo result2 = genIOStockForGift(bmsPresOutDoc, orderInfoDtlVo, customId, entryId);

            if (ObjectUtil.isNotEmpty(result2.getStdDocIds())) {
                dtlLines += result2.getStdDocIds().size();
            }
            orderResultDocVo = OrderResultDocVo.undoneAppend(orderResultDocVo, result2, orderNo, customId);
        }

        if (dtlLines == 0) {
            bmsPresOutDocMapper.deleteById(bmsPresOutDoc.getPresoutid());
            orderResultDocVo = OrderResultDocVo.failErrorMessageAppend(orderResultDocVo, "????????????????????????????????????????????????????????????", orderNo, customId);


        } else {
            bmsPresOutDoc.setDtlLines(dtlLines);
            bmsPresOutDocMapper.updateById(bmsPresOutDoc);
        }



        return orderResultDocVo;
    }


    /**
     * ???????????????????????????
     *
     * @param bmsPresOutDoc
     * @param dtl
     * @return
     */
    private OrderResultDocVo genIOStockForGift(BmsPresOutDoc bmsPresOutDoc, OrderInfoDtlVo dtl, Long customId, Integer entryId) {

        PubCustomer customer = customService.getById(customId);

        List<Long> stdDocIds = new ArrayList<>();

        String orderNo = dtl.getOrderNo();
        String orderDtId = dtl.getOrderDtlId();

        OrderResultDocVo orderResultDocVo = new OrderResultDocVo(orderNo, customer.getCustomid());
        /**
         * ??????ID
         */
        Long docId = bmsPresOutDoc.getPresoutid();

        Long goodsId = dtl.getGoodsId();

        PubGoods goods = goodsService.getById(goodsId);

        double goodQty = dtl.getNum();

        List<BmsStQtyLst> stockList = goodsService.selectStockBy(customer.getCustomid(), goodsId, dtl.getLotNo(), 1, dtl.getStorageId());


        /**
         * ?????????????????????
         */
        double totalQty = 0;

        for (int i = 0; totalQty < goodQty && i < stockList.size(); i++) {
            double currentQty = 0.0;
            BmsStIoDtlTmp stIoDtlTmp = new BmsStIoDtlTmp();

            BmsPresOutDtl bmsPresOutDtl = new BmsPresOutDtl();
            BeanUtil.copyProperties(bmsPresOutDoc,bmsPresOutDtl);

            bmsPresOutDtl.setPresoutid(docId);

            bmsPresOutDtl.setGoodsid(goodsId);

            BmsStQtyLst stQtyLst = stockList.get(i);

            Double qtyFact = stQtyLst.getQtyFact();
            /**
             * ???????????????????????????
             */
            if (qtyFact <= 0) {
                continue;
            }
            /**
             * ????????????????????????
             */
            if (qtyFact < goodQty - totalQty) {
                totalQty += qtyFact;
                currentQty = qtyFact;
            } else {
                currentQty = goodQty - totalQty;
                totalQty = goodQty;
            }


            bmsPresOutDtl.setGoodsqty(currentQty);
            bmsPresOutDtl.setStorageid(stQtyLst.getStorageid());

            bmsPresOutDtl.setSendwmsflag(1);

            bmsPresOutDtl.setRecst(0);

            bmsPresOutDtl.setGoodsstatusid(stQtyLst.getGoodsstatusid());

            bmsPresOutDtl.setGoodsuseqty(currentQty);

            bmsPresOutDtl.setGoodsqty(currentQty);

            bmsPresOutDtl.setLotid(stQtyLst.getLotid());

            bmsPresOutDtl.setGoodsuseunit(goods.getGoodsunit());

            bmsPresOutDtl.setGoodsdtlid(stQtyLst.getGoodsdetailid());

            bmsPresOutDtlMapper.insert(bmsPresOutDtl);


            stIoDtlTmp.setBatchid(stQtyLst.getBatchid());
            stIoDtlTmp.setDtlgoodsqty(currentQty);
            stIoDtlTmp.setGoodsdtlid(stQtyLst.getGoodsdetailid());
            stIoDtlTmp.setLotid(stQtyLst.getLotid());
            stIoDtlTmp.setPosid(stQtyLst.getPosid());
            stIoDtlTmp.setGoodsstatusid(stQtyLst.getGoodsstatusid());


            bmsStIoDtlTmpMapper.insert(stIoDtlTmp);


            BmsStIoDocTmp stIoDocTmp = new BmsStIoDocTmp();

            stIoDocTmp.setEntryid(entryId);
            stIoDocTmp.setGoodsid(goodsId);
            stIoDocTmp.setCompanyid(customId);
            stIoDocTmp.setCompanyname(customer.getCustomname());
            stIoDocTmp.setOutqty(currentQty);
            /**
             * ???????????????
             */
            stIoDocTmp.setComefrom(18);
            stIoDocTmp.setSourcetable(18);

            /**
             * ?????????????????????
             */
            stIoDocTmp.setPlacetable(0);

            stIoDocTmp.setStorageid(stQtyLst.getStorageid());


            stIoDocTmp.setSourceid(bmsPresOutDtl.getPresoutdtlid());


            bmsStIoDocTmpMapper.insert(stIoDocTmp);

            stdDocIds.add(stIoDocTmp.getInoutid());

            stIoDtlTmp.setInoutid(stIoDocTmp.getInoutid());

            bmsStIoDtlTmpMapper.updateById(stIoDtlTmp);

        }

        orderResultDocVo.setStdDocIds(stdDocIds);

        if (totalQty < goodQty) {
            return OrderResultDocVo.undoneAppend(orderResultDocVo, orderNo, orderDtId, goodsId, goodQty - totalQty);
        }

        return OrderResultDocVo.validReturn(orderResultDocVo, orderNo, customId);
    }

    /**
     * ????????????????????????
     */
    private OrderInfoDocVo decomposeOrderDetails(OrderInfoDocVo orderInfoDocVo, Boolean isGift) {

        OrderInfoDocVo giftOrder = new OrderInfoDocVo();

        BeanUtil.copyProperties(orderInfoDocVo, giftOrder);
        List<OrderInfoDtlVo> orderInfoDtlList = new ArrayList<>();

        orderInfoDocVo.getOrderInfoDtlList().forEach(item -> {
            PubGoods pubGoods = goodsService.getById(item.getGoodsId());

            if (ObjectUtil.equal(pubGoods.getAccflag(), 5) && isGift.equals(true)) {

                orderInfoDtlList.add(item);

            }
            if (ObjectUtil.notEqual(pubGoods.getAccflag(), 5) && isGift.equals(false)) {

                orderInfoDtlList.add(item);
            }


        });

        if (ObjectUtil.isNotEmpty(orderInfoDtlList)) {

            return giftOrder;
        }
        return null;

    }


    private OrderResultDocVo genOrderSa(OrderInfoDocVo orderInfoDocVo) {
        Long orderId = orderInfoDocVo.getOrderId();
        String orderNo = orderInfoDocVo.getOrderNo();
        Long customId = orderInfoDocVo.getCustomId();
        Integer entryId = orderInfoDocVo.getEntryId();
        Long b2bStoreId=orderInfoDocVo.getStoreId();

        PubCustomer customer = customService.getById(customId);

        BmsTrPosDef address = customService.getAddressByCustomId(customId, entryId,b2bStoreId);

        PubCustomToSaler saler = customService.getSalerByCustomId(customId, entryId);

        OrderResultDocVo orderResultDocVo = new OrderResultDocVo(orderNo, customId);
        /**
         * ??????????????????????????? ???????????????
         */
        List<OrderInfoDocVo> orderList = splitOrder(orderInfoDocVo);


        for (OrderInfoDocVo infoDocVo : orderList) {


            BmsSaDoc bmsSaDoc = new BmsSaDoc();

            String  address1=address.getAddress();
            String store;
            if(address1.indexOf(">")>0){
                 store=address1.substring(0,address1.indexOf(">"));
            }else{
                 store=address1;
            }

            bmsSaDoc.setB2bOrderNo(orderNo);
            bmsSaDoc.setB2bOrderId(orderId);
            bmsSaDoc.setB2bStoreId(b2bStoreId);

            bmsSaDoc.setB2bWriteBackFlag(1);
            bmsSaDoc.setB2bSubCustomId((long)2856);
            bmsSaDoc.setB2bAmountTotal(orderInfoDocVo.getAmountTotal());
            bmsSaDoc.setB2bAmountPay(orderInfoDocVo.getAmountPay());
            bmsSaDoc.setB2bAmountDelivery(orderInfoDocVo.getAmountDelivery());
            bmsSaDoc.setB2bAmountDiscount(orderInfoDocVo.getAmountDiscount());

            bmsSaDoc.setMemo((ObjectUtil.isNotNull(infoDocVo.getRemark()) ? "B2B??????,???" + infoDocVo.getRemark() : "") +store+ "B2B????????????" + infoDocVo.getOrderNo());

            bmsSaDoc.setZxMemo("B2B?????????" + store + "," + customer.getCustomname());

            bmsSaDoc.setUsestatus(2);
            bmsSaDoc.setSatypeid(1);
            bmsSaDoc.setExchange(1.0);
            bmsSaDoc.setSettletypeid(10);
            bmsSaDoc.setEntryid(entryId);

            bmsSaDoc.setCustomid(customId);

            bmsSaDoc.setTotal(infoDocVo.getAmountTotal());
            bmsSaDoc.setRecmoney(infoDocVo.getAmountPay());
            bmsSaDoc.setCustomname(customer.getCustomname());

            bmsSaDoc.setInvtype(customer.getInvtype());
            bmsSaDoc.setSalesdeptid(saler.getSalerdeptid());
            bmsSaDoc.setComefrom(1);
            bmsSaDoc.setApproveflag(0);

            //????????????
            bmsSaDoc.setCredate(orderInfoDocVo.getCreateDate());
            bmsSaDoc.setAssessdate(orderInfoDocVo.getCreateDate());
            bmsSaDoc.setZxBhFlag(21);

            bmsSaDoc.setDelivermethod(1);
            /**
             * ???????????????
             */
            //bmsSaDoc.setApproveflag(1);


            bmsSaDoc.setSalerid(saler.getSalerid());

            bmsSaDoc.setInputmanid(saler.getSalerid());

            bmsSaDoc.setTargetposid((long)address.getTranposid());


            bmsSaDocMapper.insert(bmsSaDoc);
            Integer dtlLines = 0;
            for (OrderInfoDtlVo orderInfoDtlVo : infoDocVo.getOrderInfoDtlList()) {
                OrderResultDocVo result2 = genIOStockForSa(bmsSaDoc, orderInfoDtlVo, customId, entryId);

                if (ObjectUtil.isNotEmpty(result2.getStdDocIds())) {
                    dtlLines += result2.getStdDocIds().size();
                }
                orderResultDocVo = OrderResultDocVo.undoneAppend(orderResultDocVo, result2, orderNo, customId);
            }

            if (dtlLines == 0) {
                bmsSaDocMapper.deleteById(bmsSaDoc.getSalesid());
                orderResultDocVo = OrderResultDocVo.failErrorMessageAppend(orderResultDocVo, "????????????????????????????????????????????????????????????", orderNo, customId);
            } else {


                bmsSaDocMapper.updateSummary(bmsSaDoc.getSalesid(), entryId);
            }

        }


        return orderResultDocVo;
    }

    private OrderResultDocVo genIOStockForSa(BmsSaDoc bmsSaDoc, OrderInfoDtlVo orderInfoDtlVo, Long customId, Integer entryId) {
        PubCustomer customer = customService.getById(customId);


        List<Long> stdDocIds = new ArrayList<>();

        String orderNo = orderInfoDtlVo.getOrderNo();
        String orderDtId = orderInfoDtlVo.getOrderDtlId();

        //Double price = orderInfoDtlVo.getPriceDiscount();
        //Double price = NumberUtil.round(orderInfoDtlVo.getAmount() / orderInfoDtlVo.getNum(), 4).doubleValue();

        OrderResultDocVo orderResultDocVo = new OrderResultDocVo(orderNo, customer.getCustomid());
        /**
         * ??????ID
         */
        Long docId = bmsSaDoc.getSalesid();

        Long goodsId = orderInfoDtlVo.getGoodsId();

        PubGoods goods = goodsService.getById(goodsId);

        //Double price=goodsService.getPrice(customId, goodsId,entryId);
        PubEntryCustomerVo  pubEntryCustomerVo=goodsService.selectBy(customId, goodsId,entryId);
        Double price=pubEntryCustomerVo.getPrice();
        Integer priceId=pubEntryCustomerVo.getPriceId();
        double goodQty = orderInfoDtlVo.getNum();
        List<BmsStQtyLst> stockList = null;


        stockList = goodsService.selectStockBy(customer.getCustomid(), goodsId, orderInfoDtlVo.getLotNo(), 1, orderInfoDtlVo.getStorageId());

        double totalQty = 0.0;


        for (int i = 0; totalQty < goodQty && i < stockList.size(); i++) {
            double currentQty = 0;
            BmsStIoDtlTmp stIoDtlTmp = new BmsStIoDtlTmp();

            BmsSaDtl bmsSaleDtl = new BmsSaDtl();

            BeanUtil.copyProperties(bmsSaDoc, bmsSaleDtl);

            BmsStQtyLst stQtyLst = stockList.get(i);

            double qtyFact = stQtyLst.getQtyFact();
            /**
             * ???????????????????????????
             */
            if (qtyFact <= 0) {
                continue;
            }
            /**
             * ????????????????????????
             */
            if (qtyFact < goodQty - totalQty) {
                totalQty += qtyFact;
                currentQty = qtyFact;
            } else {
                currentQty = goodQty - totalQty;
                totalQty = goodQty;
            }

            bmsSaleDtl.setSalesid(docId);

            bmsSaleDtl.setGoodsid(goodsId);
            bmsSaleDtl.setGoodsdtlid(stQtyLst.getGoodsdetailid());
            bmsSaleDtl.setTaxrate(goods.getSalestaxrate());
            bmsSaleDtl.setUnitprice(price);

            bmsSaleDtl.setGoodsqty(currentQty);
            bmsSaleDtl.setGoodsuseqty(currentQty);
            bmsSaleDtl.setCorrectflag(0);
            bmsSaleDtl.setDiscount(1.0);

            bmsSaleDtl.setPriceid(priceId);


            bmsSaleDtl.setGoodsuseunit(goods.getGoodsunit());

            bmsSaleDtl.setBatchid(stQtyLst.getBatchid());
            bmsSaleDtl.setLotid(stQtyLst.getLotid());
            bmsSaleDtl.setPosid(stQtyLst.getPosid());
            bmsSaleDtl.setGoodsstatusid(stQtyLst.getGoodsstatusid());


            /**
             * ?????????????????????
             */

            Double totalLine = NumberUtil.round(price * currentQty, 4).doubleValue();
            bmsSaleDtl.setTotalLine(totalLine);


            bmsSaleDtl.setStorageid(stQtyLst.getStorageid());


            bmsSaleDtl.setTotalLine(totalLine);


            bmsSaleDtl.setB2bOrderDtlId(orderInfoDtlVo.getOrderDtlId());
            bmsSaleDtl.setB2bNum(orderInfoDtlVo.getNum());
            bmsSaleDtl.setB2bPrice(orderInfoDtlVo.getPrice());
            bmsSaleDtl.setB2bPriceDiscount(price);

            if (isUseWms(stQtyLst.getStorageid(), entryId)) {
                bmsSaleDtl.setSendwmsflag(1);
            } else {
                bmsSaleDtl.setSendwmsflag(0);
            }

            bmsSaDtlMapper.insert(bmsSaleDtl);


            stIoDtlTmp.setBatchid(stQtyLst.getBatchid());
            stIoDtlTmp.setDtlgoodsqty(currentQty);
            stIoDtlTmp.setGoodsdtlid(stQtyLst.getGoodsdetailid());
            stIoDtlTmp.setLotid(stQtyLst.getLotid());
            stIoDtlTmp.setPosid(stQtyLst.getPosid());
            stIoDtlTmp.setGoodsstatusid(stQtyLst.getGoodsstatusid());


            //stIoDtlTmpList.add(stIoDtlTmp);


            BmsStIoDocTmp stIoDocTmp = new BmsStIoDocTmp();
            stIoDocTmp.setSourceid(bmsSaleDtl.getSalesdtlid());
            stIoDocTmp.setCredate(LocalDateTime.now());
            stIoDocTmp.setStorageid(stQtyLst.getStorageid());
            stIoDocTmp.setInoutflag(0);
            stIoDocTmp.setUsestatus(1);

            stIoDocTmp.setEntryid(1);


            stIoDocTmp.setGoodsid(goodsId);
            stIoDocTmp.setCompanyid(customId);
            stIoDocTmp.setCompanyname(customer.getCustomname());
            stIoDocTmp.setOutqty(currentQty);


            /**
             * ???????????????
             */
            stIoDocTmp.setComefrom(3);
            stIoDocTmp.setSourcetable(2);


            stIoDocTmp.setStorageid(stQtyLst.getStorageid());


            stIoDocTmp.setGoodsunit(goods.getGoodsunit());

            bmsStIoDocTmpMapper.insert(stIoDocTmp);
            stIoDtlTmp.setInoutid(stIoDocTmp.getInoutid());
            bmsStIoDtlTmpMapper.insert(stIoDtlTmp);

            stdDocIds.add(stIoDocTmp.getInoutid());

        }

        orderResultDocVo.setStdDocIds(stdDocIds);

        if (totalQty < goodQty) {
            return OrderResultDocVo.undoneAppend(orderResultDocVo, orderNo, orderDtId, goodsId, goodQty - totalQty);
        }

        return OrderResultDocVo.validReturn(orderResultDocVo, orderNo, customId);


    }

    private OrderResultDocVo genOrderPs(OrderInfoDocVo orderInfoDocVo) {

        String orderNo = orderInfoDocVo.getOrderNo();
        Long orderId = orderInfoDocVo.getOrderId();
        Long customId = orderInfoDocVo.getCustomId();
        Integer entryId = orderInfoDocVo.getEntryId();
        Long b2bStoreId=orderInfoDocVo.getStoreId();

        PubCustomer customer = customService.getById(customId);

        BmsTrPosDef address = customService.getAddressByCustomId(customId, entryId, b2bStoreId);

        PubCustomToSaler saler = customService.getSalerByCustomId(customId, entryId);

        OrderResultDocVo orderResultDocVo = new OrderResultDocVo(orderNo, customId);
        /**
         * ??????????????????????????? ???????????????
         */
        List<OrderInfoDocVo> orderList = splitOrder(orderInfoDocVo);

        for (OrderInfoDocVo infoDocVo : orderList) {


            GpcsPlacesupply gpcsPlacesupply = new GpcsPlacesupply();



            gpcsPlacesupply.setB2bOrderNo(orderNo);
            gpcsPlacesupply.setB2bOrderId(orderId);
            gpcsPlacesupply.setB2bAmountTotal(orderInfoDocVo.getAmountTotal());
            gpcsPlacesupply.setB2bAmountPay(orderInfoDocVo.getAmountPay());
            gpcsPlacesupply.setB2bAmountDelivery(orderInfoDocVo.getAmountDelivery());

            gpcsPlacesupply.setB2bAmountDiscount(orderInfoDocVo.getAmountDiscount());


            gpcsPlacesupply.setB2bOrderType(orderInfoDocVo.getOrderType());

            gpcsPlacesupply.setPlacedate(orderInfoDocVo.getCreateDate());

            gpcsPlacesupply.setMemo((ObjectUtil.isNotNull(infoDocVo.getRemark()) ? "B2B??????,???" + infoDocVo.getRemark() : "") + "B2B????????????" + infoDocVo.getOrderNo());


            /**
             * 7??????B2B?????????
             */
            //gpcsPlacesupply.setJdOrderType(7);

            //gpcsPlacesupply.setMemo("B2B????????????" + orderId);

            gpcsPlacesupply.setPlacepointid(customId);

            gpcsPlacesupply.setTotal(infoDocVo.getAmountTotal());

            gpcsPlacesupply.setZxBhFlag(21);

            gpcsPlacesupply.setPlacecenterid(1);

            //????????????  ????????????
            gpcsPlacesupply.setPlacemethod(7);
            gpcsPlacesupply.setUrgentflag(0);
            gpcsPlacesupply.setDocdiscount(1.0);

            gpcsPlacesupply.setUsestatus(1);


            gpcsPlacesupply.setPlacemanid(saler.getSalerid());
            //???????????? ??????
            gpcsPlacesupply.setDelivermethod(1);

            //????????????
            gpcsPlacesupplyMapper.insert(gpcsPlacesupply);
            Integer dtlLines = 0;
            for (OrderInfoDtlVo orderInfoDtlVo : infoDocVo.getOrderInfoDtlList()) {
                OrderResultDocVo result2 = genIOStockForPs(gpcsPlacesupply, orderInfoDtlVo, customId, entryId);

                if (ObjectUtil.isNotEmpty(result2.getStdDocIds())) {
                    dtlLines += result2.getStdDocIds().size();
                }
                orderResultDocVo = OrderResultDocVo.undoneAppend(orderResultDocVo, result2, orderNo, customId);
            }

            if (dtlLines == 0) {
                gpcsPlacesupplyMapper.deleteById(gpcsPlacesupply.getPlacesupplyid());
                orderResultDocVo = OrderResultDocVo.failErrorMessageAppend(orderResultDocVo, "????????????????????????????????????????????????????????????", orderNo, customId);
            } else {
                gpcsPlacesupply.setDtlLines(dtlLines);
                gpcsPlacesupplyMapper.updateById(gpcsPlacesupply);
            }

        }

        return orderResultDocVo;
    }

    private OrderResultDocVo genIOStockForPs(GpcsPlacesupply gpcsPlacesupply, OrderInfoDtlVo orderInfoDtlVo, Long customId, Integer entryId) {
        PubCustomer customer = customService.getById(customId);

        List<Long> stdDocIds = new ArrayList<>();

        String orderNo = orderInfoDtlVo.getOrderNo();
        String orderDtId = orderInfoDtlVo.getOrderDtlId();

        Double price = orderInfoDtlVo.getPriceDiscount();

        OrderResultDocVo orderResultDocVo = new OrderResultDocVo(orderNo, customer.getCustomid());
        /**
         * ??????ID
         */
        Long docId = gpcsPlacesupply.getPlacesupplyid();

        Long goodsId = orderInfoDtlVo.getGoodsId();

        PubGoods goods = goodsService.getById(goodsId);


        double goodQty = orderInfoDtlVo.getNum();
        List<BmsStQtyLst> stockList = null;

       /* if (ObjectUtil.equal(orderInfoDtlVo.getGoodsType(), 7)) {
            stockList = goodsService.selectStockBy(customer.getCustomid(), goodsId, orderInfoDtlVo.getLotNo(), false, orderInfoDtlVo.getStorageId());
        } else {*/
        stockList = goodsService.selectStockBy(customer.getCustomid(), goodsId, orderInfoDtlVo.getLotNo(), 1, orderInfoDtlVo.getStorageId());
        //}

        double totalQty = 0.0;

        for (int i = 0; totalQty < goodQty && i < stockList.size(); i++) {
            double currentQty = 0;
            BmsStIoDtlTmp stIoDtlTmp = new BmsStIoDtlTmp();
            GpcsPlacesupplydtl placesupplyDtl = new GpcsPlacesupplydtl();

            BeanUtil.copyProperties(gpcsPlacesupply,placesupplyDtl);

            BmsStQtyLst stQtyLst = stockList.get(i);
            //Integer qtyFact = stQtyLst.getQtyFact();

            double qtyFact = stQtyLst.getQtyFact();
            /**
             * ???????????????????????????
             */
            if (qtyFact <= 0) {
                continue;
            }
            /**
             * ????????????????????????
             */
            if (qtyFact < goodQty - totalQty) {
                totalQty += qtyFact;
                currentQty = qtyFact;
            } else {
                currentQty = goodQty - totalQty;
                totalQty = goodQty;
            }

            stIoDtlTmp.setBatchid(stQtyLst.getBatchid());
            stIoDtlTmp.setDtlgoodsqty(currentQty);
            stIoDtlTmp.setGoodsdtlid(stQtyLst.getGoodsdetailid());
            stIoDtlTmp.setLotid(stQtyLst.getLotid());
            stIoDtlTmp.setPosid(stQtyLst.getPosid());
            stIoDtlTmp.setGoodsstatusid(stQtyLst.getGoodsstatusid());

            BmsStIoDocTmp stIoDocTmp = new BmsStIoDocTmp();

            stIoDocTmp.setInoutflag(0);
            stIoDocTmp.setUsestatus(1);
            stIoDocTmp.setGoodsid(goodsId);
            stIoDocTmp.setCompanyid(customId);
            stIoDocTmp.setCompanyname(customer.getCustomname());
            stIoDocTmp.setOutqty(currentQty);
            /**
             * ???????????????
             */
            stIoDocTmp.setComefrom(16);
            stIoDocTmp.setSourcetable(10);

            stIoDocTmp.setPlacetable(1);

            stIoDocTmp.setStorageid(stQtyLst.getStorageid());


            stIoDocTmp.setGoodsunit(goods.getGoodsunit());

            placesupplyDtl.setGoodsqty(currentQty);





            placesupplyDtl.setPlacesupplyid(docId);


            placesupplyDtl.setTotalLine(price * currentQty);
            placesupplyDtl.setGoodsqty(currentQty);


            placesupplyDtl.setGoodsid(goodsId);
            placesupplyDtl.setGoodsdtlid(stQtyLst.getGoodsdetailid());
            //placesupplyDtl.setta(goods.getSalestaxrate());

            placesupplyDtl.setPlacepriceid(orderInfoDtlVo.getPriceId());
            placesupplyDtl.setPlaceprice(price);

            placesupplyDtl.setGoodsunit(goods.getGoodsunit());

            placesupplyDtl.setDiscount(1.0);





            placesupplyDtl.setB2bOrderDtlId(orderInfoDtlVo.getOrderDtlId());
            placesupplyDtl.setB2bNum(orderInfoDtlVo.getNum());
            placesupplyDtl.setB2bPrice(orderInfoDtlVo.getPrice());
            placesupplyDtl.setB2bPriceDiscount(orderInfoDtlVo.getPriceDiscount());

            placesupplyDtl.setZxSplitType(goods.getZxSplitType());




            gpcsPlacesupplydtlMapper.insert(placesupplyDtl);

            /**
             * ??????St????????????
             */

            GpcsPlacesupplydtlSt placesupplyDtlSt = new GpcsPlacesupplydtlSt();


            /**
             * ?????????????????????
             */
            placesupplyDtlSt.setTotalLine(price * currentQty);
            placesupplyDtlSt.setGoodsqty(currentQty);


            placesupplyDtlSt.setStorageid(stQtyLst.getStorageid());

            placesupplyDtlSt.setPlacesupplydtlid(placesupplyDtl.getPlacesupplydtlid());

            placesupplyDtlSt.setPlacepriceid(orderInfoDtlVo.getPriceId());
            placesupplyDtlSt.setPlaceprice(price);
            placesupplyDtlSt.setUnitprice(price);


            gpcsPlacesupplydtlStMapper.insert(placesupplyDtlSt);

            stIoDocTmp.setSourceid(placesupplyDtlSt.getPlacesupplydtlstid());
            stIoDocTmp.setPlacedtlid(placesupplyDtlSt.getPlacesupplydtlstid());

            stIoDocTmp.setPlacepointid(customId);


            stIoDocTmp.setEntryid(entryId);


            /**
             * ??????St??????
             */
            GpcsPlacesupplydtlStdtl gpcsPlacesupplyDtlStDtl = new GpcsPlacesupplydtlStdtl();

            gpcsPlacesupplyDtlStDtl.setBatchid(stQtyLst.getBatchid());
            gpcsPlacesupplyDtlStDtl.setLotid(stQtyLst.getLotid());
            gpcsPlacesupplyDtlStDtl.setPosid(stQtyLst.getPosid());
            gpcsPlacesupplyDtlStDtl.setGoodsstatusid(stQtyLst.getGoodsstatusid());

            gpcsPlacesupplyDtlStDtl.setPlacesupplydtlstid(placesupplyDtlSt.getPlacesupplydtlstid());




            gpcsPlacesupplydtlStdtlMapper.insert(gpcsPlacesupplyDtlStDtl);


            bmsStIoDocTmpMapper.insert(stIoDocTmp);
            stIoDtlTmp.setInoutid(stIoDocTmp.getInoutid());
            bmsStIoDtlTmpMapper.insert(stIoDtlTmp);

            stdDocIds.add(stIoDocTmp.getInoutid());

        }

        orderResultDocVo.setStdDocIds(stdDocIds);

        if (totalQty < goodQty) {
            return OrderResultDocVo.undoneAppend(orderResultDocVo, orderNo, orderDtId, goodsId, goodQty - totalQty);
        }

        return OrderResultDocVo.validReturn(orderResultDocVo, orderNo, customId);
    }

    private List<OrderInfoDocVo> splitOrder(OrderInfoDocVo orderInfoDocVo) {
        Map<String, List<OrderInfoDtlVo>> map = new HashMap<>();
        List<OrderInfoDocVo> docList = new ArrayList<>();
        List<OrderInfoDtlVo> detailList = orderInfoDocVo.getOrderInfoDtlList();
        for (int i = 0; i < detailList.size(); i++) {
            OrderInfoDtlVo dtl = detailList.get(i);
            Long goodsId = dtl.getGoodsId();
            PubGoods goods = goodsService.getById(goodsId);

            String splitFlag = goods.getZxSplitType() + "-" + goods.getSalestaxrate();

            List<OrderInfoDtlVo> list = map.get(splitFlag);
            if (ObjectUtil.isNull(list)) {
                list = new ArrayList<>();
                map.put(splitFlag, list);
            }
            list.add(dtl);
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {

            OrderInfoDocVo oneDoc = new OrderInfoDocVo();
            BeanUtil.copyProperties(orderInfoDocVo, oneDoc);
            List<OrderInfoDtlVo> oneDtlList = map.get(key);

            /**
             * ????????????
             */
            oneDoc.setOrderInfoDtlList(oneDtlList);

            docList.add(oneDoc);
        }
        return docList;
    }



    @Override
    public void confirmSaOrder(String orderNo, Integer entryId, Integer srcStatus, Integer targetStatus, List<Integer> platformList) {

        QueryWrapper<BmsSaDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BmsSaDoc::getB2bOrderNo, orderNo);
        queryWrapper.lambda().eq(BmsSaDoc::getUsestatus, srcStatus);
        queryWrapper.lambda().in(BmsSaDoc::getZxBhFlag, platformList);
        List<BmsSaDoc> bmsSaDocs = bmsSaDocMapper.selectList(queryWrapper);


        if (ObjectUtil.isNotEmpty(bmsSaDocs)) {
            bmsSaDocs.forEach(item -> {
                /**
                 * ????????????????????????????????????
                 */
                Long salesId = item.getSalesid();
                long customId = item.getCustomid();
                Long salerId = item.getSalerid();
                Long salesdeptId = item.getSalesdeptid();
                Double total = item.getTotal();
                List<BmsSaDtl> bmsSaDtls = bmsSaDtlMapper.selectListBy(salesId);

                bmsSaDocMapper.updateUsetatus(salerId, salesId);
                BmsSaConDoc bmsSaConDoc = genBmsSaConDoc(item);
                Long conId = bmsSaConDoc.getConid();
                bmsSaConDocMapper.updateBy(conId);
                BmsTrDoc bmsTrDoc = genBmsTrDoc(salesId);
                BmsTrDtl bmsTrDtl = genBmsTrDtl(bmsTrDoc);
                if (ObjectUtil.isNotEmpty(bmsSaDtls)) {

                    bmsSaDtls.forEach(bmsSaDtl -> {
                        /**
                         *
                         * peiqy ??????
                         */

                        Long trId = bmsTrDoc.getTrid();
                        Long trdtlId = bmsTrDtl.getTrdtlid();
                        bmsStIoDocTmpMapper.updateBy(trdtlId, salesId);
                        genBmsTrBackInsert(salesId, trdtlId);
                        bmsTrDtlMapper.updateByTrid(salerId, bmsTrDtl.getTrid());



                        BmsTrPickDoc bmsTrPickDoc = new BmsTrPickDoc();
                        bmsTrPickDoc.setInputmanid(salerId);
                        bmsTrPickDoc.setTrid(trId);
                        List<IncaIoDtlVo> incaIoDtlVos = bmsStIoDtlTmpMapper.selectBy(trdtlId);
                        incaIoDtlVos.forEach(incaIoDtlVo -> {
                            bmsTrPickDoc.setTranslineid(incaIoDtlVo.getTranslineid() + "");
                            bmsTrPickDoc.setZxCompanyid(customId);
                            bmsTrPickDoc.setZxCompanyname(item.getCustomname());
                            bmsTrPickDoc.setUsestatus(1);
                            bmsTrPickDoc.setPickareasid(incaIoDtlVo.getPickareasid());
                            bmsTrPickDoc.setCredate(LocalDateTime.now());
                            bmsTrPickDocMapper.insert(bmsTrPickDoc);
                            Long iodtlid = incaIoDtlVo.getIodtlid();
                            Long pickdocid = bmsTrPickDoc.getPickdocid();
                            bmsStIoDtlTmpMapper.updateBy(pickdocid, iodtlid);
                            bmsTrPickDocMapper.updateBy(pickdocid);
                        });

                        BmsSaDtlTmp bmsSaDtlTmp = new BmsSaDtlTmp();
                        bmsSaDtlTmp.setSalesdtlid(bmsSaDtl.getSalesdtlid());
                        bmsSaDtlTmpMaper.insert(bmsSaDtlTmp);


                        Long goodsId = bmsSaDtl.getGoodsid();
                        IncaGoodsCustomVo incaGoodsCustomVo = bmsCreditBillDtlMapper.queryByCust(customId, goodsId);
                        if (incaGoodsCustomVo != null && incaGoodsCustomVo.getLastsaprice() > 0) {
                            Long lastsadtlid = incaGoodsCustomVo.getLastsadtlid();
                            Double lastsaprice = incaGoodsCustomVo.getLastsaprice();
                            bmsCreditBillDtlMapper.updateByPrice(lastsadtlid, lastsaprice, customId, goodsId);
                        }
                    });

                }

                String serialno = bmsTrPickDocMapper.getSerialno();
                String waveno = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + "-" + serialno;
                bmsTrDtlMapper.updateWaveno(waveno, bmsTrDtl.getTrdtlid());
                bmsTrPickDocMapper.updateWaveno(waveno, bmsTrDtl.getTrdtlid());
                genBmsCertDtlTmp(salesId);
                if (!ObjectUtils.isNull(customId) && !ObjectUtils.isNull(salerId) && !ObjectUtils.isNull(salesdeptId)) {
                    BmsCreditBillVo bmsCreditBill = bmsCreditBillDtlMapper.queryByid(customId, salerId, salesdeptId);
                    if (!ObjectUtils.isNull(bmsCreditBill)) {
                        Long billid = bmsCreditBill.getBillid();
                        Double owemoney = bmsCreditBill.getOwemoney();
                        owemoney += total;
                        bmsCreditBillDtlMapper.updateByBillid(billid, owemoney);
                        BmsCreditBillDtl bmsCreditBillDtl = new BmsCreditBillDtl();
                        bmsCreditBillDtl.setBillid(billid);
                        bmsCreditBillDtl.setBusimoney(total);
                        bmsCreditBillDtl.setBusitype(1);
                        bmsCreditBillDtl.setSourceid(salesId);
                        bmsCreditBillDtl.setSourcetable("BMS_SA_DOC");
                        bmsCreditBillDtl.setBusicredate(LocalDateTime.now());
                        bmsCreditBillDtl.setBusiconfirmdate(LocalDateTime.now());
                        bmsCreditBillDtlMapper.insert(bmsCreditBillDtl);

                    }
                }


            });
        }


    }



    /**
     * ?????????????????????
     *
     * @param salesId
     * @return
     */
    private BmsCertDtlTmp genBmsCertDtlTmp(Long salesId) {
        Long traId = bmsCertDtlTmpMapper.getTransactionId();
        BmsCertDtlTmp bmsCertDtlTmp = new BmsCertDtlTmp();
        bmsCertDtlTmp.setSourceid(salesId);
        bmsCertDtlTmp.setSourcetable("BMS_SA_DOC");
        bmsCertDtlTmp.setAcctype(2);
        bmsCertDtlTmp.setCerttype(0);
        bmsCertDtlTmp.setTransactionid(traId);
        bmsCertDtlTmpMapper.insert(bmsCertDtlTmp);
        return bmsCertDtlTmp;
    }



    /**
     * ?????????????????????????????????
     *
     * @param salesId
     * @param
     * @return
     */

    private BmsTrBackInsert genBmsTrBackInsert(Long salesId, Long trdtlId) {
        BmsTrBackInsert bmsTrBackInsert = new BmsTrBackInsert();
        bmsTrBackInsert.setComefrom(3);
        bmsTrBackInsert.setType(1);
        bmsTrBackInsert.setSourceid(salesId);
        bmsTrBackInsert.setTrdtlid(trdtlId);
        bmsTrBackInsert.setCredate(LocalDateTime.now());
        incaTrBackInsertMapper.insert(bmsTrBackInsert);
        return bmsTrBackInsert;
    }


    /**
     * ?????????????????????
     *
     * @param
     * @return
     */
    private BmsTrDtl genBmsTrDtl(BmsTrDoc bmsTrDoc) {
        BmsTrDtl bmsTrDtl = new BmsTrDtl();
        bmsTrDtl.setTrid(bmsTrDoc.getTrid());
        bmsTrDtl.setPreparestatus(0);
        bmsTrDtl.setSignflowflag(1);
        bmsTrDtl.setStorerid(7368L);
        bmsTrDtl.setSignformman(bmsTrDoc.getSalerid());

        String employeename = pubEmployeeMapper.getEmployeename(bmsTrDoc.getSalerid());
        bmsTrDtl.setSignformmanname(employeename);
        bmsTrDtl.setSignflowtime(LocalDateTime.now());
        bmsTrDtlMapper.insert(bmsTrDtl);
        return bmsTrDtl;
    }


    /**
     * ??????????????????
     *
     * @param salesId
     * @return
     */
    private BmsTrDoc genBmsTrDoc(Long salesId) {
        BmsTrDoc bmsTrDoc = bmsTrDocMapper.selectListBy(salesId);
        bmsTrDoc.setComefrom(3);
        bmsTrDoc.setSourcetable(2);
        bmsTrDoc.setSourceid(salesId);
        bmsTrDoc.setTranpriority(1);
        bmsTrDoc.setCredate(LocalDateTime.now());
        bmsTrDoc.setTocompanyid(bmsTrDoc.getCustomid());
        bmsTrDocMapper.insert(bmsTrDoc);
        return bmsTrDoc;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param bmsSaConDtl
     * @return
     */
    private BmsSaContodoc genBmsSaConToDoc(BmsSaConDtl bmsSaConDtl) {
        BmsSaContodoc bmsSaConToDoc = new BmsSaContodoc();
        bmsSaConToDoc.setCondtlid(bmsSaConDtl.getCondtlid());
        bmsSaConToDoc.setGoodsqty(bmsSaConDtl.getGoodsqty());
        bmsSaConToDoc.setSalesdtlid(bmsSaConDtl.getSalesdtlid());
        bmsSaConToDoc.setTotalLine(bmsSaConDtl.getTotalLine());
        bmsSaConToDocMapper.insert(bmsSaConToDoc);

        return bmsSaConToDoc;
    }


    /**
     * ????????????????????????
     *
     * @param item
     * @return
     */
    private BmsSaConDoc genBmsSaConDoc(BmsSaDoc item) {
        BmsSaConDoc bmsSaConDoc = new BmsSaConDoc();
        bmsSaConDoc.setContype(2);
        bmsSaConDoc.setComefrom(2);
        bmsSaConDoc.setCustomname(item.getCustomname());
        bmsSaConDoc.setSigndate(LocalDateTime.now());
        bmsSaConDoc.setAssessdate(item.getCredate());
        bmsSaConDoc.setInvtype(item.getInvtype());
        bmsSaConDoc.setDelivermethod(item.getDelivermethod());
        bmsSaConDoc.setUsestatus(1);
        bmsSaConDoc.setConfirmdate(LocalDateTime.now());
        bmsSaConDoc.setDtlLines(item.getDtlLines());
        bmsSaConDoc.setTotal(item.getTotal());
        bmsSaConDoc.setApproveflag(item.getApproveflag());
        bmsSaConDoc.setCustomid(item.getCustomid());
        bmsSaConDoc.setSalerid(item.getSalerid());
        bmsSaConDoc.setSalesdeptid(item.getSalesdeptid());
        bmsSaConDoc.setSignmanid(item.getSalerid());
        bmsSaConDoc.setTargetposid(item.getTargetposid());
        bmsSaConDoc.setInputmanid(item.getSalerid());
        bmsSaConDoc.setEntryid(item.getEntryid());
        bmsSaConDoc.setConfirmanid(item.getSalerid());
        bmsSaConDocMapper.insert(bmsSaConDoc);
        return bmsSaConDoc;
    }


    @Override
    public void confirmPsOrder(String orderNo, Integer entryId, Integer srcStatus, Integer targetStatus, List<Integer> platformList) {

        QueryWrapper<GpcsPlacesupply> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GpcsPlacesupply::getB2bOrderNo, orderNo);
        queryWrapper.lambda().eq(GpcsPlacesupply::getUsestatus, srcStatus);
        queryWrapper.lambda().in(GpcsPlacesupply::getZxBhFlag, platformList);

        List<GpcsPlacesupply> gpcsPlacesupplies = gpcsPlacesupplyMapper.selectList(queryWrapper);

        if(ObjectUtil.isNotEmpty(gpcsPlacesupplies)){
            gpcsPlacesupplies.forEach(item->{
                /**
                 * ????????????????????????????????????
                 */





            });
        }
    }




    @Override
    public Integer getOrderStatus(String orderNo) {
        QueryWrapper<BmsSaDtl> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BmsSaDtl::getB2bOrderNo,orderNo);
        List<BmsSaDtl> bmsSaDtls = bmsSaDtlMapper.selectList(queryWrapper);

        if(ObjectUtil.isEmpty(bmsSaDtls)){
            return 1;
        }

        for (BmsSaDtl bmsSaDtl : bmsSaDtls) {
            if (ObjectUtil.isEmpty(bmsSaDtl.getWmsbackdate())) {

                return 2;
            }
        }

        return 3;

    }

    @Override
    public List<OrderDetailVo> selectOrderDetails(String orderNo) {
        return bmsSaDocMapper.selectOrderDetails(orderNo);
    }

    @Override
    public Boolean isUseWms(Long storageId, Integer entryId) {

        Integer useWms = bmsStDefMapper.getUseWms(storageId, entryId);

        if (ObjectUtil.equal(1, useWms)) {
            return true;
        } else {
            return false;
        }

    }
}
