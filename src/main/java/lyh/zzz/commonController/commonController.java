package lyh.zzz.commonController;

import lombok.extern.slf4j.Slf4j;
import lyh.zzz.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * @author LYHzzz
 * @create 2023-03-18-12:58
 */
@RestController
@RequestMapping("/common" )
@Slf4j

public class commonController {

    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //原始文件名
         String originalFilename = file.getOriginalFilename(); //a.jpg


        //UUID
        String fileUUID = UUID.randomUUID().toString();

        //后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = fileUUID + substring;

        log.info(file.toString());

        //创建一个目录
        File dir = new File(basePath);
        if (dir.exists()){
            //目录不存在
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }


    @GetMapping("/download")
    public void download(HttpServletResponse response,String name){
        //输入流
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");



            int length = 0 ;
            byte[] bytes = new byte[1024];

            while( (length = fileInputStream.read(bytes) )!= -1 ){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }



            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

}
