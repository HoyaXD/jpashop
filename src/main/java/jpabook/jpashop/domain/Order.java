package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //default 값이다 member를 조인해서 같이 가줌 = em.find() 한건일때
    @JoinColumn(name = "member_id")
    private Member member;

    //JPOL select o from order o; -> SQL select * from order 1 + n 문제  100 + 1 (order)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //order에다가 데이터를 저장하면 자동으로 저장해줌
    private List<OrderItem> orderItems = new ArrayList<>();
    //persist(orderItemsA)
    //persist(orderItemsB)
    //persist(orderItemsC)
    //persist(order)
    // cascade를 넣으면 위에 있는 것들이 자종으로 되어 persist(order)만 사용하면 된다.

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    //모든 엔티티는 persist 하면 각각 따로 persist 해줘야하는데 이렇게 하면 order만 해도 자동으로 delivery까지 persist가 된다.
    //order_date
    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //연관관계 편입 메서드
    //양방향일시 사용하면 좋음
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDeliver(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 매서드==//
    public static  Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    //주문취소
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //조회 로직
    //전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem: orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
