package com.aishare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("share_user_favorites")
public class UserFavorite {
    private Long userId;
    private Long projectId;
    private LocalDateTime createdAt;
}
