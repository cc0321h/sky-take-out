package com.sky.controller.user;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        // OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    /**
     * get all orders
     * @param orderPageQueryDTO
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO orderPageQueryDTO) {
        log.info("order page query: {}", orderPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(orderPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * get orderDetail by id
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getByOrderId(@PathVariable("id") Long orderId) {
        log.info("get order by id: {}", orderId);
        OrderVO orderVO = orderService.getByOrderId(orderId);
        return Result.success(orderVO);
    }

    /*
     * 再来一单
     * @param Id
     * @return orderSubmitVO
     */
    @PostMapping("repetition/{id}")
    @ApiOperation("再来一单")
    public Result<OrderSubmitVO> repetition(@PathVariable("id") Long id){
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

    /*
     * cancel order
     * @param orderId
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("cancel order")
    public Result cancel (@PathVariable("id") Long orderId) {
        log.info("cancel order: {}", orderId);
        orderService.cancelOrder(orderId);
        return Result.success();
    }
}
