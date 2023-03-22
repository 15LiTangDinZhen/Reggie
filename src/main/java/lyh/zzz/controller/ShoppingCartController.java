package lyh.zzz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.BaseContext;
import lyh.zzz.common.R;
import lyh.zzz.entity.Dish;
import lyh.zzz.entity.Setmeal;
import lyh.zzz.entity.ShoppingCart;
import lyh.zzz.service.DishService;
import lyh.zzz.service.SetmealService;
import lyh.zzz.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LYHzzz
 * @create 2023-03-21-20:28
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;



    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        /*
            设置用户id  指定是哪个用户的数据
            查询当前添加的菜品或套餐是否在购物车，在则 number+1，否则添加到购物车
         */

        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);


        if (dishId != null){  //判断添加的是菜品还是套餐
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);





    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

       return  R.success(list);
    }


    @DeleteMapping("/clean")
    public R<String> clean(){
        // delete from shoppingcart where userid = ...

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return  R.success("清空购物车成功");
    }


    @PostMapping("/sub")
    public R<String> sub(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);


        Integer number = shoppingCart.getNumber();

        shoppingCart.setNumber(number-1);
        if (number == 1){
            shoppingCartService.removeById(shoppingCart.getId());
            return  R.success("删除成功");
        }

        shoppingCartService.updateById(shoppingCart);
        return R.success("删除成功");




    }
}
