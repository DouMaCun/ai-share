package com.aishare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 2000, message = "评论最多2000字")
    private String content;
    private Long parentId;
}