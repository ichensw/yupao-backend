package cn.ichensw.yupao.controller;

import cn.ichensw.yupao.common.BaseResponse;
import cn.ichensw.yupao.common.ErrorCode;
import cn.ichensw.yupao.exception.BusinessException;
import cn.ichensw.yupao.model.domain.Message;
import cn.ichensw.yupao.model.domain.Team;
import cn.ichensw.yupao.model.domain.User;
import cn.ichensw.yupao.model.dto.TeamQuery;
import cn.ichensw.yupao.model.request.DeleteRequest;
import cn.ichensw.yupao.model.request.TeamAddRequest;
import cn.ichensw.yupao.model.request.TeamJoinRequest;
import cn.ichensw.yupao.model.request.TeamQuitRequest;
import cn.ichensw.yupao.model.vo.MessageVO;
import cn.ichensw.yupao.model.vo.TeamUserVO;
import cn.ichensw.yupao.service.MessageService;
import cn.ichensw.yupao.service.TeamService;
import cn.ichensw.yupao.service.UserService;
import cn.ichensw.yupao.service.UserTeamService;
import cn.ichensw.yupao.utils.RedisUtils;
import cn.ichensw.yupao.utils.ResultUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息接口
 *
 * @author csw
 */
@RestController
@RequestMapping("/message")
@Slf4j
@Api(tags = "消息接口")
public class MessageController {

    @Resource
    private MessageService messageService;
    @Resource
    private UserService userService;


    /**
     * 查询全部消息列表
     *
     * @return List<TeamUserVO>
     */
    @GetMapping("/list")
    public BaseResponse<List<MessageVO>> listMessages(HttpServletRequest request) {
        // 1. 查询消息列表
        List<MessageVO> teamList = messageService.listMessages(request);
        return ResultUtils.success(teamList);
    }

    /**
     * 查询用户的消息记录
     *
     * @return List<MessageVO>
     */
    @GetMapping("/getUserHistoryMessage")
    public BaseResponse<List<MessageVO>> getUserHistoryMessage(@RequestParam("fromUserId") Long fromUserId,
                                                               @RequestParam("toUserId") Long toUserId, HttpServletRequest request) {
        List<MessageVO> result = messageService.getUserHistoryMessage(fromUserId, toUserId);
        return ResultUtils.success(result);
    }


    /**
     * 查询聊天室的消息列表
     *
     * @return List<MessageVO>
     */
    @GetMapping("/getRoomHistoryMessage")
    public BaseResponse<List<MessageVO>> getRoomHistoryMessage(@RequestParam("fromUserId") Long fromUserId,
                                                               @RequestParam("toRoomId") Long toRoomId, HttpServletRequest request) {
        List<MessageVO> result = messageService.getRoomHistoryMessage(fromUserId, toRoomId);
        return ResultUtils.success(result);
    }


}
