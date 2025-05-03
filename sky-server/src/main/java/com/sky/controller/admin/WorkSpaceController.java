package com.sky.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台相关接口")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;
    
    /**
     * 营业数据查询
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("营业数据查询")
    public Result<BusinessDataVO> businessData() {
        log.info("营业数据查询");
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * 订单统计
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("订单统计")
    public Result<OrderOverViewVO> overviewOrders() {
        log.info("订单统计");
        OrderOverViewVO orderOverViewVO = workSpaceService.getOrderOverview();
        return Result.success(orderOverViewVO);
    }
    
    /**
     * 菜品统计
     * @return
     */
    @GetMapping("overviewDishes")
    @ApiOperation("菜品统计")
    public Result<DishOverViewVO> overviewDishes() {
        log.info("菜品统计");
        DishOverViewVO dishesOverview = workSpaceService.getDishesOverview();
        return Result.success(dishesOverview);
    }

    /**
     * 套餐统计
     * @return
     */
    @GetMapping("overviewSetmeals")
    @ApiOperation("套餐统计")
    public Result<SetmealOverViewVO> overviewSetmeals() {
        log.info("套餐统计");
        SetmealOverViewVO setmealOverViewVO = workSpaceService.getSetmealOverview();
        return Result.success(setmealOverViewVO);
    }
}
