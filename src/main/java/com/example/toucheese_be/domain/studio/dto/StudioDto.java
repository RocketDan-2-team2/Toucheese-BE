package com.example.toucheese_be.domain.studio.dto;

import com.example.toucheese_be.domain.studio.entity.Portfolio;
import com.example.toucheese_be.domain.studio.entity.Studio;
import com.example.toucheese_be.domain.studio.entity.StudioImage;
import com.example.toucheese_be.domain.studio.entity.constant.StudioImageType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudioDto {
    private Long id;
    private String name;
    private String profileImg;
    private Double popularity;
    private List<String> portfolios;

    public StudioDto(
            Long id,
            String name,
            String profileImg,
            Double popularity
    ) {
        this.id = id;
        this.name = name;
        this.profileImg = profileImg;
        this.popularity = popularity;
        this.portfolios = new ArrayList<>();
    }
}
