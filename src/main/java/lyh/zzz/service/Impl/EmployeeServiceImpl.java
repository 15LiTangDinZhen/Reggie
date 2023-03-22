package lyh.zzz.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lyh.zzz.entity.Employee;
import lyh.zzz.mapper.EmployeeMapper;
import lyh.zzz.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author LYHzzz
 * @create 2023-03-12-18:40
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
