package lyh.zzz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.CustomException;
import lyh.zzz.common.R;
import lyh.zzz.dto.DishDto;
import lyh.zzz.entity.Category;
import lyh.zzz.entity.Dish;
import lyh.zzz.entity.DishFlavor;
import lyh.zzz.service.CategoryService;
import lyh.zzz.service.DishFlavorService;
import lyh.zzz.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author LYHzzz
 * @create 2023-03-18-14:20
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private RedisTemplate redisTemplate;


    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dto){
       log.info(dto.toString());

       dishService.saveWithFlavor(dto);

       return R.success("新增成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page , int pageSize,String name){

        System.out.println("page = " + page);

        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");


        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {


            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id

            Category category = categoryService.getById(categoryId);

            if (category != null){

                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return  dishDto;

        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);

    }


    //根据id查询对应菜品信息和口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return  R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dto){


        //清除所有缓存
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);

        String key = "dish_" + dto.getCategoryId() + "_" + dto.getStatus();
        redisTemplate.delete(key);

        dishService.updateWithFlavor(dto);


        return R.success("修改成功");
    }



    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dtos = null;

        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        //从缓存中获取数据
        dtos = (List<DishDto>)redisTemplate.opsForValue().get(key);

        if (dtos != null){
            return R.success(dtos);
        }


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);


        List<Dish> list = dishService.list(queryWrapper);

         dtos = list.stream().map((item) -> {


            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id

            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品id
            Long DishId = item.getId();

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,DishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return  dishDto;

        }).collect(Collectors.toList());


         redisTemplate.opsForValue().set(key,dtos,60, TimeUnit.MINUTES);

        return R.success(dtos);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }


    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,Long[] ids){
        for (int i = 0; i < ids.length; i++) {

            Long id = ids[i];
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);

        }

        return R.success("修改成功");

    }

   @DeleteMapping
    public R<String> deleteDish(Long[] ids){

       for (int i = 0; i < ids.length; i++) {
           Long id = ids[i];
           Dish dish = dishService.getById(id);
           if (dish.getStatus() == 1){
               throw new  CustomException("该菜品正在销售");
           }
           dishService.removeById(id);
       }

       return R.success("删除成功");
   }

}
