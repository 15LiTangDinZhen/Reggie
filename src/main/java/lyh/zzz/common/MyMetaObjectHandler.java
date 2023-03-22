package lyh.zzz.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sun.xml.internal.ws.api.config.management.ManagedEndpointFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 * @author LYHzzz
 * @create 2023-03-16-13:59
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override    //插入操作自动填充
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());

        metaObject.setValue("updateTime",LocalDateTime.now());

        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override   //更新操作自动填充
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());

        metaObject.setValue("updateTime",LocalDateTime.now());

        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
