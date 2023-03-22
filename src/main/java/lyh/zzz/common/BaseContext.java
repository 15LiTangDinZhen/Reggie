package lyh.zzz.common;

/**
 * @author LYHzzz
 * @create 2023-03-16-19:01
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static
    Long getCurrentId(){
        return threadLocal.get();
    }
}
