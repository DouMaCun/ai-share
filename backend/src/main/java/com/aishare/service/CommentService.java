package com.aishare.service;

import com.aishare.dto.CommentDTO;
import com.aishare.vo.CommentVO;
import java.util.List;

public interface CommentService {
    List<CommentVO> listByProjectId(Long projectId);
    CommentVO create(CommentDTO dto, Long projectId);
    void delete(Long commentId);
}