package jpabasic.inheritanceMapping;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") // 기본 값 Entity name
public class Album extends Item{
    private String artist;
}
