package com.sky.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkSpaceService {

    /**
     * 营业数据查询
     * @param end 
     * @param begin 
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 订单统计
     * @return
     */
    OrderOverViewVO getOrderOverview();

    /**
     * 菜品统计
     * @return
     */ 
    DishOverViewVO getDishesOverview();

    /**
     * 套餐统计
     * @return
     */
    SetmealOverViewVO getSetmealOverview();

}
