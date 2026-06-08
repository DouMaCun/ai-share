package com.aishare.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectTagMapper {

    @Insert("INSERT IGNORE INTO share_project_tags(project_id, tag_id) VALUES(#{projectId}, #{tagId})")
    void insert(Long projectId, Long tagId);

    @Delete("DELETE FROM share_project_tags WHERE project_id = #{projectId}")
    void deleteByProjectId(Long projectId);
}
