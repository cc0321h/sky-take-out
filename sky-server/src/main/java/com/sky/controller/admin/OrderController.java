package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "B端订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * get all orders
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("order page query: {}", ordersPageQueryDTO);
        PageResult pageQuery = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageQuery);
    }
    
    /**
     * get order statistics
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * get order details
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable Long id) {
        OrderVO orderVO = orderService.getByOrderId(id);
        return Result.success(orderVO);
    }

    /**
     * confirm order
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * reject order
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * delivery order
     * @param id
     * @return
     */
    @PutMapping("delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        orderService.delivery(id);
        return Result.success();
    }
    

    /**
     * cancel order
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }
    
    /**
     * complete order
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        orderService.complete(id);
        return Result.success();
    }
}
