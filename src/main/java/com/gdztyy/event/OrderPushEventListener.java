package com.gdztyy.event;

import cn.hutool.core.util.ObjectUtil;
import com.gdztyy.api.service.OrderService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderPushEventListener implements ApplicationListener<OrderPushEvent> {

    @Resource
    OrderService orderService;

    @Override
    @Async
    public void onApplicationEvent(OrderPushEvent event) {
        if(ObjectUtil.isNull(event.getOrderInfoDocVo())){
            return;
        }
        orderService.genOrder(event.getOrderInfoDocVo());
    }
}
