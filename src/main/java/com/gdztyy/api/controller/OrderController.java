package com.gdztyy.api.controller;

import com.gdztyy.api.service.OrderService;
import com.gdztyy.api.vo.*;
import com.gdztyy.config.security.B2bUserDetails;
import com.gdztyy.event.OrderPushEvent;
import org.redisson.misc.Hash;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    OrderService orderService;

    @RequestMapping("/push")
    @PreAuthorize("isAuthenticated()")
    public OrderResultDocVo pushOrder(@RequestBody OrderInfoDocVo orderInfoDocVo) {
        B2bUserDetails userDetails = (B2bUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        orderInfoDocVo.setCustomId(userDetails.getCustomId());
        orderInfoDocVo.setEntryId(1);

        applicationEventPublisher.publishEvent(new OrderPushEvent(orderInfoDocVo,"inca-api"));

        return new OrderResultDocVo(orderInfoDocVo.getOrderNo(),orderInfoDocVo.getCustomId(),"订单已经下发!,状态请稍候获取！！！");
    }

    /**
     * 1 没有订单  2  还未出库  3 已经出库
     *
     * @param orderNo
     * @return
     */
    @RequestMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public OrderStatusVo getOrderStatus(@RequestParam("order_no") String orderNo) {
        OrderStatusVo orderStatusVo = new OrderStatusVo();
        orderStatusVo.setOrderNo(orderNo);
        orderStatusVo.setStatus(orderService.getOrderStatus(orderNo));
        return orderStatusVo;
    }

    /**
     * 1 没有订单  2  还未出库  3 已经出库
     *
     * @param orderNo
     * @return
     */
    @RequestMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public OrderDocVo getOrderDetails(@RequestParam("order_no") String orderNo) {
        OrderDocVo orderDocVo = new OrderDocVo();
        orderDocVo.setOrderNo(orderNo);
        orderDocVo.setDetailList(orderService.selectOrderDetails(orderNo));
        return orderDocVo;
    }

}
