package com.aishare.vo;

import com.aishare.entity.Tag;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectVO {
    private Long id;
    private String title;
    private String description;
    private String coverUrl;
    private String projectUrl;
    private String githubUrl;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer viewCount;
    private Integer status;
    private LocalDateTime createdAt;
    private UserVO author;
    private List<Tag> tags;
    private Boolean liked;
    private Boolean favorited;
}
