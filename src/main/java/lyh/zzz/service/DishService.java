package lyh.zzz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lyh.zzz.dto.DishDto;
import lyh.zzz.entity.Dish;
import org.springframework.stereotype.Service;

/**
 * @author LYHzzz
 * @create 2023-03-16-21:28
 */

public interface DishService extends IService<Dish> {

    //操作两张表 dish dishflavor

    public void saveWithFlavor(DishDto dishDto);

    //根据id查询。。。
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

}
