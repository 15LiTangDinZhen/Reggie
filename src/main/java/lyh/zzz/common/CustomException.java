package lyh.zzz.common;

import org.springframework.remoting.RemoteTimeoutException;

/**
 * 自定义业务异常
 * @author LYHzzz
 * @create 2023-03-17-10:22
 */
public class CustomException extends RuntimeException {

    public CustomException (String msg){
        super(msg);
    }
}
