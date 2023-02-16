package jpabook.jpashop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent")
    private Category parent;

    @OneToMany(mappedBy = "PARENT_ID")
    private List<Category> child = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Item> items = new ArrayList<>();
}
