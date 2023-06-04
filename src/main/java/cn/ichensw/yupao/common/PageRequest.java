package cn.ichensw.yupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求参数
 * @author iChen
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -4693859718795130980L;
    /**
     * 每页大小
     */
    protected int pageSize = 10;
    /**
     * 页码
     */
    protected int pageNum = 1;

}
