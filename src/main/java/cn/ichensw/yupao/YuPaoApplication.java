package cn.ichensw.yupao;

import cn.ichensw.yupao.ws.ChatRoomWebSocket;
import cn.ichensw.yupao.ws.UserChatWebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ichensw
 */
@SpringBootApplication
@MapperScan("cn.ichensw.yupao.mapper")
@EnableScheduling
public class YuPaoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YuPaoApplication.class, args);
        UserChatWebSocket.setApplicationContext(context);
        ChatRoomWebSocket.setApplicationContext(context);
    }

}
