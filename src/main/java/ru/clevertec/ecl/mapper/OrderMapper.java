package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.order.OrderDTO;
import ru.clevertec.ecl.mapper.api.DTOMapper;
import ru.clevertec.ecl.model.Order;


@Mapper
public interface OrderMapper extends DTOMapper<Order, OrderDTO> {

}
