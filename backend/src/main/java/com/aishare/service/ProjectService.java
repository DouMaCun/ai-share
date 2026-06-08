package com.aishare.service;

import com.aishare.common.PageResult;
import com.aishare.dto.ProjectDTO;
import com.aishare.dto.ProjectQueryDTO;
import com.aishare.vo.ProjectVO;
import java.util.List;

public interface ProjectService {
    ProjectVO create(ProjectDTO dto);
    ProjectVO update(Long id, ProjectDTO dto);
    void delete(Long id);
    ProjectVO getById(Long id);
    PageResult<ProjectVO> list(ProjectQueryDTO query);
    List<ProjectVO> myProjects();
    void like(Long projectId);
    void favorite(Long projectId);
}
