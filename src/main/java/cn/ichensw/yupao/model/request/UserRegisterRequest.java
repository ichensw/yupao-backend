package cn.ichensw.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author yupi
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 3269392436678779287L;
    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
