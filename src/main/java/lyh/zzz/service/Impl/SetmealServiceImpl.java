package lyh.zzz.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lyh.zzz.common.CustomException;
import lyh.zzz.common.R;
import lyh.zzz.dto.SetmealDto;
import lyh.zzz.entity.Setmeal;
import lyh.zzz.entity.SetmealDish;
import lyh.zzz.mapper.SetmealMapper;
import lyh.zzz.service.SetmealDishService;
import lyh.zzz.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LYHzzz
 * @create 2023-03-16-21:31
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    
    @Autowired
    private SetmealService setmealService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);


    }

    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        /*
            只能删除停售套餐 查询套餐状态
            如果可以删除，先删套餐表数据 然后删除关系表中对象
            如果不能删除，抛出异常
         */


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in( Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("套餐正在售卖中");
        }

        this.removeByIds(ids);  //删套餐表中数据 setmeal

        //删关系表中数据  setmealDish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);




    }

    @Override
    public SetmealDto getWithSetmealDish(Long id) {
        Setmeal setmeal = setmealService.getById(id);

        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SetmealDish::getId,setmeal.getId());

        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }


}
