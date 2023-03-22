package lyh.zzz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.R;
import lyh.zzz.entity.Category;
import lyh.zzz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LYHzzz
 * @create 2023-03-16-19:33
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("分类：{}",category);
        categoryService.save(category);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);

        //条件构造器 排序
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Category::getSort);

        //分页查询
        categoryService.page(pageInfo,queryWrapper);


        return  R.success(pageInfo);
    }


    //根据id删除分类
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，id为 ：",ids
        );

        categoryService.remove(ids);

        return R.success("删除成功");


    }

    @PutMapping   //根据id修改分类信息
    public  R<String> update(@RequestBody Category category){
        categoryService.updateById(category);

        return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();

        lambdaQueryWrapper.eq(category.getType()!= null,Category::getType,category.getType());

        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getSort);

        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return  R.success(list);
    }

}
