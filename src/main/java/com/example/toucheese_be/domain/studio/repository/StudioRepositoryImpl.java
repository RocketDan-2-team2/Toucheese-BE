package com.example.toucheese_be.domain.studio.repository;

import static org.hibernate.query.sqm.produce.function.StandardArgumentsValidators.between;

import com.example.toucheese_be.domain.item.entity.QItem;
import com.example.toucheese_be.domain.item.entity.constant.ItemCategory;
import com.example.toucheese_be.domain.studio.dto.StudioDto;
import com.example.toucheese_be.domain.studio.dto.StudioSearchFilterDto;
import com.example.toucheese_be.domain.studio.entity.QPortfolio;
import com.example.toucheese_be.domain.studio.entity.QStudio;
import com.example.toucheese_be.domain.studio.entity.QStudioImage;
import com.example.toucheese_be.domain.studio.entity.Studio;
import com.example.toucheese_be.domain.studio.entity.constant.PriceFilter;
import com.example.toucheese_be.domain.studio.entity.constant.Region;
import com.example.toucheese_be.domain.studio.entity.constant.StudioImageType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudioRepositoryImpl implements StudioRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QStudio studio = QStudio.studio;
    private final QItem item = QItem.item;
    private final QPortfolio portfolio = QPortfolio.portfolio;
    private final QStudioImage studioImage = QStudioImage.studioImage;

    @Override
    public Page<StudioDto> getStudioListWithPages(
            Long conceptId,
            StudioSearchFilterDto dto,
            Pageable pageable
    ) {
        // 기본 조건 : 컨셉 ID 로 필터링
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(studio.concept.id.eq(conceptId));

        // 지역 필터
        if (dto.getRegion() != null && !dto.getRegion().isEmpty()) {
            BooleanBuilder regionBuilder = new BooleanBuilder();
            for (Region region : dto.getRegion()) {
                regionBuilder.or(studio.address.containsIgnoreCase(region.getDescription()));
            }
            builder.and(regionBuilder);
        }
        // 인기 필터
        if (dto.getPopularity() != null) {
            builder.and(studio.popularity.goe(dto.getPopularity().getMinRating()));
        }

        // 가격 필터
        if (dto.getPriceFilter() != null) {
            Integer minPrice = dto.getPriceFilter().getMinPrice(); // 최소 가격
            Integer maxPrice = dto.getPriceFilter().getMaxPrice(); // 최대 가격

            var priceQuery = JPAExpressions.select(item.price.min())
                    .from(item)
                    .where(
                            item.studio.eq(studio)
                                    .and(item.itemCategory.eq(ItemCategory.PROFILE_PHOTO))
                    );

            builder.and(priceQuery.goe(minPrice).and(priceQuery.loe(maxPrice)));

        }
        // 1. StudioDto 기본 정보 조회
        List<StudioDto> studioDtos = jpaQueryFactory.select(
                        Projections.constructor(StudioDto.class,
                                studio.id,
                                studio.name,
                                JPAExpressions.select(studioImage.imageUrl)
                                        .from(studioImage)
                                        .where(studioImage.studio.eq(studio).and(studioImage.type.eq(
                                                StudioImageType.PROFILE)))
                                        .limit(1),
                                studio.popularity
                        )
                )
                .distinct() // 중복 제거를 위해 distinct() 추가
                .from(studio)
                .leftJoin(studio.portfolios, portfolio)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 포트폴리오 URL 그룹핑
        Map<Long, List<String>> portfolioMap = jpaQueryFactory
                .select(portfolio.studio.id, portfolio.imageUrl)
                .from(portfolio)
                .where(portfolio.studio.id.in(studioDtos.stream().map(StudioDto::getId).collect(Collectors.toList())))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(portfolio.studio.id),
                        Collectors.mapping(tuple -> tuple.get(portfolio.imageUrl), Collectors.toList())
                ));

        // 3. StudioDto에 포트폴리오 URL 리스트 매핑
        studioDtos.forEach(studioDto ->
                studioDto.setPortfolios(portfolioMap.getOrDefault(studioDto.getId(), List.of())));

        // 총 개수 계산 (count query)
        // 페이징 처리 시 쿼리 분리 : count 쿼리와 데이터 조회 쿼리를 분리하여 효율적으로 페이징 처리
        long total = jpaQueryFactory.selectFrom(studio)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(studioDtos, pageable, total);
    }
}
