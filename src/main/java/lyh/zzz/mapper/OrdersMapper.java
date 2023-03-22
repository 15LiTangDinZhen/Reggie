package lyh.zzz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lyh.zzz.common.BaseContext;
import lyh.zzz.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LYHzzz
 * @create 2023-03-22-17:11
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
