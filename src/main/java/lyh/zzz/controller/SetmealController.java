package lyh.zzz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.R;
import lyh.zzz.dto.SetmealDto;
import lyh.zzz.entity.Category;
import lyh.zzz.entity.Setmeal;
import lyh.zzz.service.CategoryService;
import lyh.zzz.service.SetmealDishService;
import lyh.zzz.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LYHzzz
 * @create 2023-03-19-15:29
 *
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String>  save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.saveWithDish(setmealDto);
        return R.success("成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize, String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page,pageSize);


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> dtoList = records.stream().map( (item) ->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();

                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;

        }).collect(Collectors.toList());

        Page<SetmealDto> setmealDtoPage = dtoPage.setRecords(dtoList);


        return R.success(setmealDtoPage);

    }



    @DeleteMapping  //删除  操作两张表
    @CacheEvict(value = "setmealCache",allEntries = true) //清理setmealCache下所有缓存
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids : {}",ids);

        setmealService.removeWithDish(ids);




        return R.success("删除成功");
    }


    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,Long[] ids){
        for (int i = 0; i < ids.length; i++) {

            Long id = ids[i];
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);

        }

        return R.success("修改成功");

    }

    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDish = setmealService.getWithSetmealDish(id);


        return R.success(setmealDish);
    }



    /*
        根据条件查询套餐数据
     */

    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);


        return R.success(setmealList);
    }

}
