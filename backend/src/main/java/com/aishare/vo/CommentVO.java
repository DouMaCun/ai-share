package com.aishare.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private String content;
    private UserVO author;
    private LocalDateTime createdAt;
}