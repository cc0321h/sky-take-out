package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * get all orders
     * @param orderPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO orderPageQueryDTO);

    /**
     * get order by id
     * @param orderId
     * @return
     */
    @Select("select * from orders where id = #{orderId}")
    Orders getById(Long orderId);

    /**
     * get order ToBeConfirmed
     * @return
     */
    @Select("select count(id) from orders where status = 2")
    Integer getToBeConfirmed();

    /**
     * get order Confirmed
     * @return
     */
    @Select("select count(id) from orders where status = 3")
    Integer getConfirmed();

    /**
     * get order ToBeInProgress
     * @return
     */
    @Select("select count(id) from orders where status = 4")
    Integer getToBeInProgres();

    /**
     * get order by status and timeOut
     * @param pendingPayment
     * @param time
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndTimeOut(Integer status, LocalDateTime time);

    /**
     * turnover statistics
     * @param begin
     * @param end
     * @return
     */
    @Select("select sum(amount) from orders where status = #{status} and order_time between #{begin} and #{end}")
    Double getTurnover(Map map);

    /**
     * get total order number
     * @param map
     * @return
     */
    @Select("select count(id) from orders where order_time between #{begin} and #{end}")
    Integer getToralOrderNum(Map map);

    /**
     * get completed order number
     * @param map
     * @return
     */
    @Select("select count(id) from orders where status = #{status} and order_time between #{begin} and #{end}")
    Integer getCompletedOrderNum(Map map);

    /**
     * get top 10
     * @param map
     * @return
     */
    @Select("select od.name, sum(od.number) number from orders o, order_detail od where o.id = od.order_id and o.status = #{status} and o.order_time between #{begin} and #{end} group by od.name order by number desc limit 10")
    List<GoodsSalesDTO> getTop10(Map map);
    
}
