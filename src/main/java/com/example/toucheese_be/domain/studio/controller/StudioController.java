package com.example.toucheese_be.domain.studio.controller;

import com.example.toucheese_be.domain.studio.dto.StudioSearchFilterDto;
import com.example.toucheese_be.domain.studio.dto.StudioDto;
import com.example.toucheese_be.domain.studio.service.StudioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/studio")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;

    // TODO : 전체 컨셉 조회
    @GetMapping("/")
    public void getConcepts() {

    }

    // TODO : 컨셉 별 스튜디오 전체 조회
    @GetMapping("/{conceptId}")
    public ResponseEntity<List<StudioDto>> getStudiosByConcept(
            @PathVariable
            Long conceptId
    ) {
        return ResponseEntity.ok(studioService.getStudiosByConcept(conceptId));
    };


    // TODO: 컨셉 별 스튜디오 조건 (인기, 가격, 거리) 검색
    @PostMapping("/{conceptId}/search")
    public ResponseEntity<Page<StudioDto>> getStudiosByConceptAndFilters(
            @PathVariable
            Long conceptId,
            @RequestBody(required = false)
            StudioSearchFilterDto dto,
            Pageable pageable
    ) {
        return ResponseEntity.ok(studioService.getStudiosByConceptFilters(conceptId, dto, pageable));
    }

}
