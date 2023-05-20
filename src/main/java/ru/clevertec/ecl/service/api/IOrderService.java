package ru.clevertec.ecl.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.order.OrderResponse;

public interface IOrderService {

    OrderResponse makeOrder(Integer userId, Integer certificateId);

    Page<OrderResponse> getUserOrders(Integer userId, Pageable pageable);
}
