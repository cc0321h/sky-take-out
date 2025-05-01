package com.sky.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DishServiceImpl implements DishService{

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    public List<DishFlavor> setDishFlavorDishId(List<DishFlavor> flavors ,Long id) {
        for(DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        return flavors;
    }

     /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增菜品
     * @param dish
     */
    @Transactional // 多表数据添加，增加事务注解，保证数据一致性
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        
        // 复制属性
        BeanUtils.copyProperties(dishDTO, dish);
        // 保存菜品
        dishMapper.insert(dish);

        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            setDishFlavorDishId(flavors, id);
            dishFlavorMapper.insertBanch(flavors);
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    public void deleteBanch(List<Long> ids) {
        
        ids.forEach(id->{
            //根据id获取菜品
            Dish dish = dishMapper.getById(id);
            //判断当前菜品是否能删除--是否存在起售的菜单中
            if (dish.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            //判断当前菜品是否能删除--是否被套餐引用
            Integer countByDishId = setmealMapper.countByDishId(id);
            if(countByDishId > 0) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            //删除菜品
            dishMapper.deleteByDishId(id);
            //删除菜品口味
            dishFlavorMapper.deleteByDishId(id);
        });
    }

    /**
     * 根据id禁用和启用菜品
     * @param status
     * @param id
     * @return
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        
        dishMapper.update(dish);
    }

    /**
     * 查询菜品详情
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.listByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //修改菜品表的基本信息
        dishMapper.update(dish);

        //删除菜品的口味信息
        dishFlavorMapper.deleteByDishId(dish.getId());

        //添加菜品的口味信息
        if(dishDTO.getFlavors() == null || dishDTO.getFlavors().size() == 0){
            return;
        }
        List<DishFlavor> flavors = dishDTO.getFlavors();
        setDishFlavorDishId(flavors, dish.getId());
        dishFlavorMapper.insertBanch(dishDTO.getFlavors());
    }

    /**
     * 查询菜品列表
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
       return dishMapper.list(categoryId);
    }

     /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish.getCategoryId());

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.listByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
