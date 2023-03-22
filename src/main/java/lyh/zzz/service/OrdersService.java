package lyh.zzz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lyh.zzz.entity.Orders;

/**
 * @author LYHzzz
 * @create 2023-03-22-17:12
 */
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
