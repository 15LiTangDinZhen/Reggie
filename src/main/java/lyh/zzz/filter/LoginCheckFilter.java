package lyh.zzz.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.BaseContext;
import lyh.zzz.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * 检查用户是否完成登录
 * @author LYHzzz
 * @create 2023-03-13-19:50
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //匹配器
    public static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String URI = request.getRequestURI();

        log.info("拦截到请求：{}",request.getRequestURI());
        //定义不与要处理的路径
        String[] urls = new String[]{
          "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(URI, urls);
        if (check){
            filterChain.doFilter(request,response);//放行
            return;
        }

        if( request.getSession().getAttribute("employee")!=null){

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);//放行
            return;
        }
        if( request.getSession().getAttribute("user")!=null){

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);//放行
            return;
        }

        //通过输出流方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;



    }

    public  boolean check(String URI,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, URI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
