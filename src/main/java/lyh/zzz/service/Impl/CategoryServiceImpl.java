package lyh.zzz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lyh.zzz.common.CustomException;
import lyh.zzz.entity.Category;
import lyh.zzz.entity.Dish;
import lyh.zzz.entity.Setmeal;
import lyh.zzz.mapper.CategoryMapper;
import lyh.zzz.service.CategoryService;
import lyh.zzz.service.DishService;
import lyh.zzz.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LYHzzz
 * @create 2023-03-16-19:31
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    //根据id删除分类 ，删除之前判断
    @Override
    public void remove(Long ids) {
        //查询当前分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0){
            //已关联
            throw new CustomException("已关联菜品");
        }


        //查询当前分类是否关联套餐

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);

        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if (count2 > 0){
            throw new CustomException("已关联套餐");
        }

        super.removeById(ids);


        //删除
    }
}
