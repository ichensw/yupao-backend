package cn.ichensw.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户加入队伍请求体
 *
 * @author csw
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 7439932736364355584L;
    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
