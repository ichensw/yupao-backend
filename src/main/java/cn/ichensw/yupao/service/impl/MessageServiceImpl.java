package cn.ichensw.yupao.service.impl;

import cn.ichensw.yupao.model.domain.Team;
import cn.ichensw.yupao.model.domain.User;
import cn.ichensw.yupao.model.domain.UserTeam;
import cn.ichensw.yupao.model.vo.MessageVO;
import cn.ichensw.yupao.service.TeamService;
import cn.ichensw.yupao.service.UserService;
import cn.ichensw.yupao.service.UserTeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ichensw.yupao.model.domain.Message;
import cn.ichensw.yupao.service.MessageService;
import cn.ichensw.yupao.mapper.MessageMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
* @author Admin
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2023-05-28 15:29:53
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @Override
    public List<MessageVO> listMessages(HttpServletRequest request) {
        List<MessageVO> result = new ArrayList<MessageVO>();
        // 获取当前用户
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getUserId();
        List<Message> messageList = this.query()
                .eq("receive_user_id", userId)
                .groupBy("send_user_id")
                .orderByAsc("send_time")
                .list();
        List<UserTeam> userTeamList = userTeamService.query()
                .eq("user_id", userId)
                .list();
        userTeamList.forEach(userTeam -> {
            Long teamId = userTeam.getTeamId();
            List<Message> roomMessageList = this.query()
                    .eq("receive_user_id", teamId)
                    .groupBy("receive_user_id")
                    .orderByAsc("send_time")
                    .list();
            messageList.addAll(roomMessageList);
        });
        messageList.forEach(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            // 判断接收方类型是私聊还是群聊，群聊则需要查询 聊天室信息（头像、名称等）
            Integer receiveType = message.getReceiveType();
            if (receiveType == 1) {
                Team team = teamService.getById(message.getReceiveUserId());
                messageVO.setAvatarUrl(team.getTeamImage());
                messageVO.setUserName(team.getName());
            } else {
                // 通过发送方id拿到头像和姓名并赋值
                User user = userService.getById(message.getSendUserId());
                messageVO.setAvatarUrl(user.getAvatarUrl());
                messageVO.setUserName(user.getUsername());
            }
            result.add(messageVO);
        });
        return result;
    }

    @Override
    public List<MessageVO> getUserHistoryMessage(Long fromUserId, Long toUserId) {
        List<MessageVO> result = new ArrayList<>();
        List<Message> messageList = this.query()
                .and(i -> i.eq("receive_user_id", toUserId).eq("send_user_id", fromUserId))
                .or(i -> i.and(j -> j.eq("receive_user_id", fromUserId).eq("send_user_id", toUserId)))
                .orderByAsc("send_time")
                .list();

        messageList.forEach(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            // 通过发送方id查询头像和姓名
            User sendUserInfo = userService.getById(message.getSendUserId());
            messageVO.setUserName(sendUserInfo.getUsername());
            messageVO.setAvatarUrl(sendUserInfo.getAvatarUrl());
            result.add(messageVO);
        });
        return result;
    }

    @Override
    public List<MessageVO> getRoomHistoryMessage(Long fromUserId, Long toRoomId) {
        List<MessageVO> result = new ArrayList<>();
        // 查询房间号下的所有消息
        List<Message> messageList = this.query()
                .eq("receive_user_id", toRoomId)
                .orderByAsc("send_time")
                .list();

        messageList.forEach(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            // 通过发送方id查询头像和姓名
            User sendUserInfo = userService.getById(message.getSendUserId());
            messageVO.setUserName(sendUserInfo.getUsername());
            messageVO.setAvatarUrl(sendUserInfo.getAvatarUrl());
            result.add(messageVO);
        });
        return result;
    }
}




