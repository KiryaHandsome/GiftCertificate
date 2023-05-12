package ru.clevertec.ecl.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.order.OrderDTO;
import ru.clevertec.ecl.model.Order;

public interface IOrderService {

    OrderDTO makeOrder(Integer userId, Integer certificateId);

    Page<OrderDTO> getUserOrders(Integer userId, Pageable pageable);
}
