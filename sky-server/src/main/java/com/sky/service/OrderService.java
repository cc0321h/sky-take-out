package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.*;

public interface OrderService {

    /**
     * 用户下单
     * 
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * 
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * 
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * get all orders
     * 
     * @param orderPageQueryDTO
     * @return
     */
    PageResult pageQuery(OrdersPageQueryDTO orderPageQueryDTO);

    /**
     * get orderDetail by id
     * 
     * @param orderId
     * @return
     */
    OrderVO getByOrderId(Long orderId);

    /*
     * 再来一单
     * 
     * @param Id
     * 
     * @return
     */
    void repetition(Long id);

    /*
     * cancel order
     * 
     * @param orderId
     * 
     * @return
     */
    void cancelOrder(Long orderId);

    /**
     * get order statistics
     * 
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * confirm order
     * 
     * @param ordersConfirmDTO
     * @return
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * reject order
     * 
     * @param ordersRejectionDTO
     * @return
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * delivery order
     * 
     * @param id
     * @return
     */
    void delivery(Long id);

    /**
     * cancel order
     * 
     * @param ordersCancelDTO
     * @return
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);
    
     /**
     * complete order
     * @param id
     * @return
     */
    void complete(Long id);

}
