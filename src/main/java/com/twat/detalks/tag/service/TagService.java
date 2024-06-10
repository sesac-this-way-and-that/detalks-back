package com.twat.detalks.tag.service;

import com.twat.detalks.tag.entity.TagEntity;
import com.twat.detalks.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService {
    @Autowired
    private TagRepository tagsRepository;

    // 모든 태그 목록 조회
    public List<TagEntity> getAllTags() {
        return tagsRepository.findAll();
    }

    // 태그 이름으로 태그 조회
    public TagEntity getTagByName(String tagName) {
        return tagsRepository.findByTagName(tagName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다."));
    }

    public TagEntity createTag(String tagName) {
        tagsRepository.findByTagName(tagName).ifPresent(tag -> {
            throw new RuntimeException("중복된 태그명입니다.");
        });

        TagEntity newTag = TagEntity.builder()
                .tagName(tagName)
                .build();

        return tagsRepository.save(newTag);
    }
}
