package lyh.zzz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.R;
import lyh.zzz.entity.Employee;
import lyh.zzz.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author LYHzzz
 * @create 2023-03-12-18:45
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null){
            return  R.error("登陆失败");
        }

        if (!emp.getPassword().equals(password)){
            return  R.error("登陆失败");
        }

        if(emp.getStatus() == 0){
            return  R.error("登陆失败");
        }

        request.getSession().setAttribute("employee",emp.getId());

        return R.success(employee);


    }

    @PostMapping("/logout")  //退出
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的当前登录员工id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    @PostMapping //新增员工
    public  R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工:{}",employee.toString());
        //设置初始密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置创建时间、更新时间
        //employee.setCreateTime(LocalDateTime.now());
       // employee.setUpdateTime(LocalDateTime.now());

        //设置最后创建者、更新者
      //Long empId = (Long) request.getSession().getAttribute("employee");
       // employee.setCreateUser(empId);
       // employee.setUpdateUser(empId);

        //保存
        employeeService.save(employee);


        return R.success("新增员工成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {}","pageSize = {}","name = {}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);


        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);


    }


    //根据id修改员工信息
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        Long empId =(Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);

        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return  R.success("修改成功");

    }

    @GetMapping("/{id}")
    public R<Employee> GetById(@RequestBody Long id){

        Employee employee = employeeService.getById(id);

        if (employee == null){
            return R.error("没查到");
        }

        return R.success(employee);
    }

}
