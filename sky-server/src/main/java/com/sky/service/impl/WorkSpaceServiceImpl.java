package com.sky.service.impl;
    
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 营业数据查询
     * @return
     */
    public BusinessDataVO getBusinessData() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", Orders.COMPLETED);
        // 营业额
        Double turnover = orderMapper.getTurnover(map);
        // 总订单量
        Integer orderCount = orderMapper.getToralOrderNum(map);
        // 有效订单量
        Integer validOrderCount = orderMapper.getOrderNumByStatusAndTime(map);
        // 订单完成率
        Double orderCompletionRate = validOrderCount * 1.0/ orderCount;
        // 平均客单价
        Double unitPrice = turnover / validOrderCount;
        // 新增用户
        Integer newUserCount = userMapper.getNewUserSum(map);

        return BusinessDataVO.builder()
            .turnover(turnover)
            .validOrderCount(validOrderCount)
            .orderCompletionRate(orderCompletionRate)
            .unitPrice(unitPrice)
            .newUsers(newUserCount)
            .build();
    }
    
    /**
     * 订单统计
     * @return
     */
    public OrderOverViewVO getOrderOverview() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.getOrderNumByStatusAndTime(map);
        map.put("status", Orders.CONFIRMED);
        Integer confirmedOrders = orderMapper.getOrderNumByStatusAndTime(map);
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.getOrderNumByStatusAndTime(map);
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.getOrderNumByStatusAndTime(map);
        
        return OrderOverViewVO.builder()
            .waitingOrders(waitingOrders)
            .deliveredOrders(confirmedOrders)
            .completedOrders(completedOrders)
            .cancelledOrders(cancelledOrders)
            .build();
        
    }

    /**
     * 菜品统计
     * @return
     */
    public DishOverViewVO getDishesOverview() {
        Integer enableDishCount = dishMapper.getDishCountByStatus(StatusConstant.ENABLE);
        Integer disableDishCount = dishMapper.getDishCountByStatus(StatusConstant.DISABLE);
        return DishOverViewVO.builder()
            .sold(enableDishCount)
            .discontinued(disableDishCount)
            .build();
    }

    /**
     * 套餐统计
     * @return
     */
    public SetmealOverViewVO getSetmealOverview() {
        Integer enableSetmealCount = setmealMapper.getSetmealCountByStatus(StatusConstant.ENABLE);
        Integer disableSetmealCount = setmealMapper.getSetmealCountByStatus(StatusConstant.DISABLE);
        return SetmealOverViewVO.builder()
            .sold(enableSetmealCount)
            .discontinued(disableSetmealCount)
            .build();
    }
    
    
}
