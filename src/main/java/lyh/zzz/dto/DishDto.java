package lyh.zzz.dto;


import lombok.Data;
import lyh.zzz.entity.Dish;
import lyh.zzz.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
