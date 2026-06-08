package com.aishare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class ProjectDTO {
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 200, message = "标题最多200字")
    private String title;

    @Size(max = 5000, message = "描述最多5000字")
    private String description;

    private String coverUrl;
    private String projectUrl;
    private String githubUrl;
    private List<String> screenshots;
    private List<Long> tagIds;
    private Integer status; // 1:publish 0:draft
}
