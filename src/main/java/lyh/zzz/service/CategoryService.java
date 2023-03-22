package lyh.zzz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lyh.zzz.entity.Category;


/**
 * @author LYHzzz
 * @create 2023-03-16-19:30
 */

public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
