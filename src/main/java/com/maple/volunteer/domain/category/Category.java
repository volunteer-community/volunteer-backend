package com.maple.volunteer.domain.category;

import com.maple.volunteer.domain.community.Community;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 카테고리 ID

    private String type; // 카테고리 이름

    @OneToMany(mappedBy = "category")
    private List<Community> communityList;

    @Builder
    public Category(String type) {
        this.type = type;
    }

    public void categoryUpdate(String changeCategoryType) {
        this.type = changeCategoryType;
    }
}
