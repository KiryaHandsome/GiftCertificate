package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.order.OrderRequest;
import ru.clevertec.ecl.dto.order.OrderResponse;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.Tag;


@Mapper
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    OrderRequest toRequest(Order order);

    Order toEntity(OrderRequest request);

    Tag toEntity(OrderResponse response);
}
