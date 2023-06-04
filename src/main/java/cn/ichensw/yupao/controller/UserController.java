package cn.ichensw.yupao.controller;

import cn.ichensw.yupao.common.BaseResponse;
import cn.ichensw.yupao.common.ErrorCode;
import cn.ichensw.yupao.constant.UserConstant;
import cn.ichensw.yupao.exception.BusinessException;
import cn.ichensw.yupao.model.domain.User;
import cn.ichensw.yupao.model.request.UserLoginRequest;
import cn.ichensw.yupao.service.UserService;
import cn.ichensw.yupao.utils.RedisUtils;
import cn.ichensw.yupao.utils.ResultUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author Jucce
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录参数
     * @param request          当前会话
     * @return 结果
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码为空");
        }

        User userInfo = userService.userLogin(userAccount, userPassword, request);
//        return new BaseResponse<User>(0, user, "ok");
        return ResultUtils.success(userInfo);
    }

    /**
     * 退出登录
     *
     * @param request 当前会话
     * @return 结果
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户登录态
     *
     * @param request 当前会话
     * @return 结果
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        // 1. 获取会话session中的User用户信息
        User currentUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 2. 查询新的用户信息返回
        long userId = currentUser.getUserId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/getUserById")
    public BaseResponse<User> getUserById(Integer userId) {
        User user = userService.getUserById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 通过标签查找用户
     *
     * @param tagNameList 标签名称列表
     * @return 结果
     */
    @GetMapping("/search/tags")
    public BaseResponse<Page<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList,
                                                     @RequestParam(required = false) String username,
                                                     @RequestParam(value = "pageSize", required = true) long pageSize,
                                                     @RequestParam(value = "pageNum", required = true) long pageNum) {
        Page<User> list = userService.searchUsersByTags(tagNameList, username, pageSize, pageNum);
        return ResultUtils.success(list);
    }

    /**
     * 用户推荐
     *
     * @param request 当前会话
     * @return 结果
     */
    @GetMapping("/recommend")
    public BaseResponse<List<User>> recommendUsers(
            @RequestParam(value = "pageSize", required = false) long pageSize,
            @RequestParam(value = "pageNum", required = false) long pageNum, HttpServletRequest request) {
        List<User> userPage = userService.recommendUsers(pageSize == 0 ? 8 : pageSize, pageNum < 0 ? 0 : pageNum, request);
        return ResultUtils.success(userPage);
    }

    /**
     * 用户推荐 - 分页版
     *
     * @param request
     * @return 用户请求
     */
    @GetMapping("/recommend/page")
    public BaseResponse<List<User>> recommendUsersByPage(
            @RequestParam("pageSize") long pageSize,
            @RequestParam("pageNum") long pageNum, HttpServletRequest request) {
        List<User> userPage = userService.recommendUsers(pageSize, pageNum, request);
        return ResultUtils.success(userPage);
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        // 1. 校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 补充校验，如果用户没有传任何需要更新的值（新值和旧值比较），不用执行更新语句
        // 2. 校验权限
        User loginUser = userService.getLoginUser(request);
        // 3. 触发更新
        Integer flag = userService.updateUser(user, loginUser);
        return ResultUtils.success(flag);
    }

    /**
     * 根据标签匹配用户
     *
     * @param num     匹配数量
     * @param request 当前会话
     * @return 结果
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num, user));
    }
}
