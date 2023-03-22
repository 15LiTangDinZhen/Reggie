package lyh.zzz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lyh.zzz.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LYHzzz
 * @create 2023-03-12-18:38
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
