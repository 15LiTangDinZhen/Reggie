package lyh.zzz.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lyh.zzz.entity.ShoppingCart;
import lyh.zzz.mapper.ShoppingCartMapper;
import lyh.zzz.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author LYHzzz
 * @create 2023-03-21-20:27
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
