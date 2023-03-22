package lyh.zzz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lyh.zzz.dto.SetmealDto;
import lyh.zzz.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LYHzzz
 * @create 2023-03-16-21:31
 */

public interface SetmealService extends IService<Setmeal> {

    //新增套餐
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    public SetmealDto getWithSetmealDish(Long id);
}
