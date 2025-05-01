package com.sky.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    @CacheEvict(cacheNames = "dishCache", key = "#dishDTO.categoryId")
    public Result add(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除菜品：{}", ids);
        dishService.deleteBanch(ids);
        return Result.success();
    }

    /**
     * 禁用和启用菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("禁用和启用菜品")
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("禁用和启用菜品：{}, {}", status, id);
        dishService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 查询菜品详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("查询菜品详情")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("查询菜品详情：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新菜品")
    @CacheEvict(cacheNames = "dishCache", allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("更新菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * @param categoryId
     * @return
     */
    @GetMapping("list")
    @ApiOperation("获取菜品列表")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
}
