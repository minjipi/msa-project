package com.minji.msa.order.domain.entity;

import com.minji.msa.order.domain.vo.OrderVO;

public class OrderEntity {
    private OrderVO orderVO;

    public OrderVO getOrderVO() {
        return orderVO;
    }

    public void setOrderVO(OrderVO orderVO) {
        this.orderVO = orderVO;
    }
}
