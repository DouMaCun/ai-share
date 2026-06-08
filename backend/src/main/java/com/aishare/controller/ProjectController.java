package com.aishare.controller;

import com.aishare.common.PageResult;
import com.aishare.common.Result;
import com.aishare.dto.ProjectDTO;
import com.aishare.dto.ProjectQueryDTO;
import com.aishare.service.ProjectService;
import com.aishare.vo.ProjectVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public Result<PageResult<ProjectVO>> list(ProjectQueryDTO query) {
        return Result.ok(projectService.list(query));
    }

    @GetMapping("/{id}")
    public Result<ProjectVO> getById(@PathVariable Long id) {
        return Result.ok(projectService.getById(id));
    }

    @PostMapping
    public Result<ProjectVO> create(@Valid @RequestBody ProjectDTO dto) {
        return Result.ok(projectService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<ProjectVO> update(@PathVariable Long id, @Valid @RequestBody ProjectDTO dto) {
        return Result.ok(projectService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return Result.ok();
    }

    @GetMapping("/mine")
    public Result<List<ProjectVO>> myProjects() {
        return Result.ok(projectService.myProjects());
    }

    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable Long id) {
        projectService.like(id);
        return Result.ok();
    }

    @PostMapping("/{id}/favorite")
    public Result<Void> favorite(@PathVariable Long id) {
        projectService.favorite(id);
        return Result.ok();
    }
}
