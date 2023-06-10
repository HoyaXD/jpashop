package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) //ORDINAL은 사용하면 안된다. 아래에는 READY, COMP 이런식으로 구조를 작성하였는데 중간에 READY, xxxx,COMP 중간에 껴있으면 디비조회가 제대로 되지 않는다. 그렇기에 꼭 STRING으로 변경
    private DeliveryStatus status; //READY, COMP

}
