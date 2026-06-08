package com.aishare.service.impl;

import com.aishare.common.BusinessException;
import com.aishare.dto.CommentDTO;
import com.aishare.entity.Comment;
import com.aishare.entity.User;
import com.aishare.mapper.CommentMapper;
import com.aishare.mapper.UserMapper;
import com.aishare.service.CommentService;
import com.aishare.util.UserContext;
import com.aishare.vo.CommentVO;
import com.aishare.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Override
    public List<CommentVO> listByProjectId(Long projectId) {
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getProjectId, projectId)
                        .orderByDesc(Comment::getCreatedAt));
        return comments.stream().map(this::buildVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentVO create(CommentDTO dto, Long projectId) {
        Long userId = UserContext.get();
        Comment comment = new Comment();
        comment.setProjectId(projectId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        commentMapper.insert(comment);
        return buildVO(comment);
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        Long userId = UserContext.get();
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) throw new BusinessException(404, "评论不存在");
        if (!comment.getUserId().equals(userId)) throw new BusinessException(403, "无权删除");
        commentMapper.deleteById(commentId);
    }

    private CommentVO buildVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setCreatedAt(comment.getCreatedAt());

        User author = userMapper.selectById(comment.getUserId());
        if (author != null) {
            UserVO userVO = new UserVO();
            userVO.setId(author.getId());
            userVO.setUsername(author.getUsername());
            userVO.setEmail(author.getEmail());
            userVO.setAvatar(author.getAvatar());
            userVO.setBio(author.getBio());
            vo.setAuthor(userVO);
        }
        return vo;
    }
}