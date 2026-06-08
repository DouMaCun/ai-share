package com.aishare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("share_user_likes")
public class UserLike {
    private Long userId;
    private Long projectId;
    private LocalDateTime createdAt;
}
