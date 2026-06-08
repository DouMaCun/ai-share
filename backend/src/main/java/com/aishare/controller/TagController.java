package com.aishare.controller;

import com.aishare.common.Result;
import com.aishare.entity.Tag;
import com.aishare.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagMapper tagMapper;

    @GetMapping
    public Result<List<Tag>> list() {
        return Result.ok(tagMapper.selectList(null));
    }
}
