package lyh.zzz.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lyh.zzz.entity.AddressBook;
import lyh.zzz.mapper.AddressBookMapper;
import lyh.zzz.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author LYHzzz
 * @create 2023-03-21-5:28
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
