package com.aishare.service.impl;

import com.aishare.common.BusinessException;
import com.aishare.common.PageResult;
import com.aishare.dto.ProjectDTO;
import com.aishare.dto.ProjectQueryDTO;
import com.aishare.entity.*;
import com.aishare.mapper.*;
import com.aishare.service.ProjectService;
import com.aishare.util.UserContext;
import com.aishare.vo.ProjectVO;
import com.aishare.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final TagMapper tagMapper;
    private final ProjectTagMapper projectTagMapper;
    private final UserLikeMapper userLikeMapper;
    private final UserFavoriteMapper userFavoriteMapper;

    @Override
    @Transactional
    public ProjectVO create(ProjectDTO dto) {
        Long userId = UserContext.get();
        Project project = new Project();
        fillProject(project, dto);
        project.setUserId(userId);
        project.setLikeCount(0);
        project.setFavoriteCount(0);
        project.setViewCount(0);
        project.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        projectMapper.insert(project);
        saveProjectTags(project.getId(), dto.getTagIds());
        return buildVO(project, userId);
    }

    @Override
    @Transactional
    public ProjectVO update(Long id, ProjectDTO dto) {
        Long userId = UserContext.get();
        Project project = getProjectOrThrow(id);
        if (!project.getUserId().equals(userId)) throw new BusinessException(403, "无权操作");
        fillProject(project, dto);
        if (dto.getStatus() != null) project.setStatus(dto.getStatus());
        projectMapper.updateById(project);
        projectTagMapper.deleteByProjectId(id);
        saveProjectTags(id, dto.getTagIds());
        return buildVO(project, userId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long userId = UserContext.get();
        Project project = getProjectOrThrow(id);
        if (!project.getUserId().equals(userId)) throw new BusinessException(403, "无权操作");
        projectMapper.deleteById(id);
        projectTagMapper.deleteByProjectId(id);
    }

    @Override
    public ProjectVO getById(Long id) {
        Project project = getProjectOrThrow(id);
        project.setViewCount(project.getViewCount() + 1);
        projectMapper.updateById(project);
        return buildVO(project, UserContext.get());
    }

    @Override
    public PageResult<ProjectVO> list(ProjectQueryDTO query) {
        Page<Project> page = new Page<>(query.getPage(), query.getSize());
        IPage<Project> result = projectMapper.selectPage(page, query);
        Long currentUserId = UserContext.get();
        List<ProjectVO> vos = result.getRecords().stream()
                .map(p -> buildVO(p, currentUserId))
                .collect(Collectors.toList());
        return PageResult.of(result.getTotal(), vos);
    }

    @Override
    public List<ProjectVO> myProjects() {
        Long userId = UserContext.get();
        List<Project> projects = projectMapper.selectList(
                new LambdaQueryWrapper<Project>()
                        .eq(Project::getUserId, userId)
                        .orderByDesc(Project::getCreatedAt));
        return projects.stream().map(p -> buildVO(p, userId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void like(Long projectId) {
        Long userId = UserContext.get();
        Project project = getProjectOrThrow(projectId);
        LambdaQueryWrapper<UserLike> wrapper = new LambdaQueryWrapper<UserLike>()
                .eq(UserLike::getUserId, userId).eq(UserLike::getProjectId, projectId);
        UserLike existing = userLikeMapper.selectOne(wrapper);
        if (existing != null) {
            userLikeMapper.delete(wrapper);
            project.setLikeCount(Math.max(0, project.getLikeCount() - 1));
        } else {
            UserLike like = new UserLike();
            like.setUserId(userId);
            like.setProjectId(projectId);
            userLikeMapper.insert(like);
            project.setLikeCount(project.getLikeCount() + 1);
        }
        projectMapper.updateById(project);
    }

    @Override
    @Transactional
    public void favorite(Long projectId) {
        Long userId = UserContext.get();
        Project project = getProjectOrThrow(projectId);
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getUserId, userId).eq(UserFavorite::getProjectId, projectId);
        UserFavorite existing = userFavoriteMapper.selectOne(wrapper);
        if (existing != null) {
            userFavoriteMapper.delete(wrapper);
            project.setFavoriteCount(Math.max(0, project.getFavoriteCount() - 1));
        } else {
            UserFavorite fav = new UserFavorite();
            fav.setUserId(userId);
            fav.setProjectId(projectId);
            userFavoriteMapper.insert(fav);
            project.setFavoriteCount(project.getFavoriteCount() + 1);
        }
        projectMapper.updateById(project);
    }

    private Project getProjectOrThrow(Long id) {
        Project project = projectMapper.selectById(id);
        if (project == null) throw new BusinessException(404, "项目不存在");
        return project;
    }

    private void fillProject(Project project, ProjectDTO dto) {
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setCoverUrl(dto.getCoverUrl());
        project.setProjectUrl(dto.getProjectUrl());
        project.setGithubUrl(dto.getGithubUrl());
    }

    private void saveProjectTags(Long projectId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return;
        tagIds.forEach(tagId -> projectTagMapper.insert(projectId, tagId));
    }

    private ProjectVO buildVO(Project project, Long currentUserId) {
        ProjectVO vo = new ProjectVO();
        vo.setId(project.getId());
        vo.setTitle(project.getTitle());
        vo.setDescription(project.getDescription());
        vo.setCoverUrl(project.getCoverUrl());
        vo.setProjectUrl(project.getProjectUrl());
        vo.setGithubUrl(project.getGithubUrl());
        vo.setLikeCount(project.getLikeCount());
        vo.setFavoriteCount(project.getFavoriteCount());
        vo.setViewCount(project.getViewCount());
        vo.setStatus(project.getStatus());
        vo.setCreatedAt(project.getCreatedAt());
        vo.setTags(tagMapper.selectByProjectId(project.getId()));

        User author = userMapper.selectById(project.getUserId());
        if (author != null) {
            UserVO userVO = new UserVO();
            userVO.setId(author.getId());
            userVO.setUsername(author.getUsername());
            userVO.setAvatar(author.getAvatar());
            userVO.setBio(author.getBio());
            vo.setAuthor(userVO);
        }

        if (currentUserId != null) {
            vo.setLiked(userLikeMapper.selectCount(new LambdaQueryWrapper<UserLike>()
                    .eq(UserLike::getUserId, currentUserId)
                    .eq(UserLike::getProjectId, project.getId())) > 0);
            vo.setFavorited(userFavoriteMapper.selectCount(new LambdaQueryWrapper<UserFavorite>()
                    .eq(UserFavorite::getUserId, currentUserId)
                    .eq(UserFavorite::getProjectId, project.getId())) > 0);
        } else {
            vo.setLiked(false);
            vo.setFavorited(false);
        }
        return vo;
    }
}
