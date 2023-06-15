package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;

    //공통 상품
    private String name;
    private int price;
    private int stockQuantity;

    //책
    private String author;
    private String isbn;
}
