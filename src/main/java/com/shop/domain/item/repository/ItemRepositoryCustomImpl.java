package com.shop.domain.item.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.item.constant.ItemSellStatus;
import com.shop.domain.item.entity.Item;
import com.shop.domain.item.entity.QItem;
import com.shop.domain.itemimg.entity.QItemImg;
import com.shop.web.controller.dto.request.ItemSearchRequestDto;
import com.shop.web.controller.dto.response.MainItemResponseDto;
import com.shop.web.controller.dto.response.QMainItemResponseDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.shop.domain.item.entity.QItem.item;

/**
 * QueryDSL + Spring Data JPA 사용을 위해 사용자 정의 인터페이스 ItemRepositoryCustom 구현
 */
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 판매 상태에 따른 조건 쿼리 생성: 판매중(SELL), 품절(품절)
     * @param searchSellStatus
     * @return
     */
    private BooleanExpression searchSellStatus(ItemSellStatus searchSellStatus) {
        // 결과값이 null이면 null을 리턴한다. 결과값이 null이면 where절에서 해당 조건은 무시
        return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
    }

    /**
     * 특정 날짜를 기준으로 해당 날짜 이후 데이터 조회 쿼리 생성
     * @param searchDateType
     * @return
     */
    private BooleanExpression regDateAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return item.regDate.after(dateTime);
    }

    /**
     * 상품명 혹은 상품 등록자 ID 기반 조회(Like) 조건 쿼리 생성
     * @param searchBy
     * @param searchQuery
     * @return
     */
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemName", searchBy)) {
            return item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    /**
     * 메인 페이지에서 검색창에 특정 상품을 조회하는 경우 사용
     * 최초 '/' 접속 시에는 검색어가 없기 때문에 null로 초기화 하여 조건에서 제외
     * @param searchQuery
     * @return
     */
    private BooleanExpression searchByItemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchRequestDto itemSearchRequestDto, Pageable pageable) {
        QueryResults<Item> itemQueryResults = queryFactory
                .selectFrom(item) // 타겟 Entity(SQL에서 FROM TABLE과 동일, JPA는 Entity 대상 쿼리 질의)
                .where(searchSellStatus(itemSearchRequestDto.getSearchSellStatus()), // where 조건에 ',' 기준 메서드 넣어주면 'and' 조건으로 인식
                        regDateAfter(itemSearchRequestDto.getSearchDateType()),
                        searchByLike(itemSearchRequestDto.getSearchBy(), itemSearchRequestDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset()) // 데이터를 가지고 올 '시작 인덱스' 지정
                .limit(pageable.getPageSize()) // 한 번에 가지고 올 최대 개수 지정
                .fetchResults();

        List<Item> contents = itemQueryResults.getResults();
        long total = itemQueryResults.getTotal();
        return new PageImpl<>(contents, pageable, total); // contents, pageable, total
    }

    /**
     * 메인 페이지 Item 내역 조회
     * @param itemSearchRequestDto
     * @param pageable
     * @return
     */
    @Override
    public Page<MainItemResponseDto> getMainItemPage(ItemSearchRequestDto itemSearchRequestDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QueryResults<MainItemResponseDto> results = queryFactory
                .select(
                        new QMainItemResponseDto(
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
//                .where(item.itemName.like("%" + itemSearchRequestDto.getSearchQuery() + "%"))
                .where(searchByItemNameLike(itemSearchRequestDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemResponseDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
