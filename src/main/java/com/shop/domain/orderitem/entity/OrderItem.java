package com.shop.domain.orderitem.entity;

import com.shop.domain.item.entity.Item;
import com.shop.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Table(name = "order_item")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
