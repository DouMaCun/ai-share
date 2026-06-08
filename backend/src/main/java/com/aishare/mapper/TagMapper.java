package com.aishare.mapper;

import com.aishare.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Select("SELECT t.* FROM share_tags t INNER JOIN share_project_tags pt ON t.id = pt.tag_id WHERE pt.project_id = #{projectId}")
    List<Tag> selectByProjectId(Long projectId);
}
