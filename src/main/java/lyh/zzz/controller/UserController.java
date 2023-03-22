package lyh.zzz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import lyh.zzz.Utils.ValidateCodeUtils;
import lyh.zzz.common.R;
import lyh.zzz.entity.User;
import lyh.zzz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author LYHzzz
 * @create 2023-03-20-14:14
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")  //发送验证码
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

/*
    获取手机号
    生成随机四位验证码
    调用阿里云短信服务API完成发送短信
    将生成的验证码保存到session中
 */

        String phone = user.getPhone();

        if (!StringUtils.isEmpty(phone)){

            String code = ValidateCodeUtils.generateValidateCode(4).toString();


            System.out.println("发出的验证码 ： "+code);

            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            session.setAttribute("phone",phone);
            session.setAttribute("code",code);
            session.setMaxInactiveInterval(99999);

            System.out.println("保存的验证码 ："+session.getAttribute("code"));

            return R.success("手机验证码已发送");

        }

        return R.error("验证码发送失败");
    }


    @PostMapping("/login")
    public R<User> userLogin(@RequestBody Map map ,HttpSession session){
        log.info(map.toString());

        /*
            获取手机号
            获取验证码
            进行验证码比对  页面提交的验证码与session保存的验证码
            比对成功，登陆成功
            判断当前手机号是否在user表中，不在则自动完成注册
         */

        String phone = map.get("phone").toString();

        String code1 = map.get("code").toString();

        String code2 = session.getAttribute("code").toString();
        System.out.println(code2);

        if(code2 != null && code2.equals(code1)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);

            if (user == null){
                user = new User();

                user.setPhone(phone);
                user.setStatus(1);

                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }



        return R.error("登陆失败");
    }
}
