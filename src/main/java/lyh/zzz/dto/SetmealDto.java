package lyh.zzz.dto;


import lombok.Data;
import lyh.zzz.entity.Setmeal;
import lyh.zzz.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
