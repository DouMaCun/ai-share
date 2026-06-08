package com.aishare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("share_comments")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Long userId;
    private String content;
    @TableLogic
    private Integer deleted;
    private LocalDateTime createdAt;
}