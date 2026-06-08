package com.aishare.controller;

import com.aishare.common.Result;
import com.aishare.dto.CommentDTO;
import com.aishare.service.CommentService;
import com.aishare.vo.CommentVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/projects/{projectId}/comments")
    public Result<List<CommentVO>> list(@PathVariable Long projectId) {
        return Result.ok(commentService.listByProjectId(projectId));
    }

    @PostMapping("/api/projects/{projectId}/comments")
    public Result<CommentVO> create(@PathVariable Long projectId, @Valid @RequestBody CommentDTO dto) {
        return Result.ok(commentService.create(dto, projectId));
    }

    @DeleteMapping("/api/comments/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.ok();
    }
}