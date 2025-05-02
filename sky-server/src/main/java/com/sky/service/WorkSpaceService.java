package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkSpaceService {

    /**
     * 营业数据查询
     * @return
     */
    BusinessDataVO getBusinessData();

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
