package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.vo.OrderVO;

@Mapper
public interface OrderDetailMapper {

    void insertBatch(List<OrderDetail> orderDetailList);

    Page<OrderVO> pageQuery(OrdersPageQueryDTO orderPageQueryDTO);

    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);

}
