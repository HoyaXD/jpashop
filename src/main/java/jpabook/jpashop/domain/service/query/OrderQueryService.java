package jpabook.jpashop.domain.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> orderV3() {

        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());

        return result;
    }
}
