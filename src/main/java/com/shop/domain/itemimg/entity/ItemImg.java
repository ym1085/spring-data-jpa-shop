package com.shop.domain.itemimg.entity;

import com.shop.domain.BaseEntity;
import com.shop.domain.item.entity.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "item_img")
@Entity
public class ItemImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로
    private String repimgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY) // 1:1 해도 상관 없지 않나?
    @JoinColumn(name = "item_id")
    private Item item;

    /* 이미지 정보 업데이트 메서드 */
    public void updateItemImg(String imgName, String oriImgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
