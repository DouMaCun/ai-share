package com.aishare.dto;

import lombok.Data;

@Data
public class ProjectQueryDTO {
    private String keyword;
    private Long tagId;
    private String sort; // newest | hottest | mostLiked
    private int page = 1;
    private int size = 12;
}
