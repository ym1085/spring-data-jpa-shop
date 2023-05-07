package com.shop.domain.order.repository;

import com.shop.domain.order.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *  조건이 복잡하지 않은 query는 @Query로 처리, 아닌 경우 Querydsl 사용
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /* 회원 email 기반 주문 이력 조회 */
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    /* 회원 email 기반 주문 개수가 몇개인지 조회 */
    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);

}
