package com.aishare.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("share_projects")
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String coverUrl;
    private String projectUrl;
    private String githubUrl;
    private String screenshots;
    private Long userId;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer viewCount;
    private Integer status;
    @TableLogic
    private Integer deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
