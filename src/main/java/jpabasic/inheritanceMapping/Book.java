package jpabasic.inheritanceMapping;

import javax.persistence.Entity;

@Entity
public class Book extends Item{
    private String author;
    private int isbn;
}
