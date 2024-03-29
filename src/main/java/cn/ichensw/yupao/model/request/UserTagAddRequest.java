package cn.ichensw.yupao.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 用户登录请求体
 *
 * @author Jucce
 * @date 2022/12/7
 */
@Data
public class UserTagAddRequest implements Serializable {

    private static final long serialVersionUID = 6121458871274540023L;
    /**
     * 标签
     */
    private String tag;
    /**
     * 用户ID
     */
    private Long userId;

}
