package lyh.zzz.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lyh.zzz.entity.User;
import lyh.zzz.mapper.UserMapper;
import lyh.zzz.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author LYHzzz
 * @create 2023-03-20-14:13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
