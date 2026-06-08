package com.aishare.mapper;

import com.aishare.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aishare.dto.ProjectQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    IPage<Project> selectPage(@Param("page") Page<Project> page,
                              @Param("q") ProjectQueryDTO query);
}
