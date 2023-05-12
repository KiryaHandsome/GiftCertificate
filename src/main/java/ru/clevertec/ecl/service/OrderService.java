package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.order.OrderDTO;
import ru.clevertec.ecl.mapper.GiftCertificateResponseMapper;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.api.IOrderService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificateService certificateService;
    private final GiftCertificateResponseMapper certificateMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO makeOrder(Integer userId, Integer certificateId) {
        User user = userService.find(userId);
        GiftCertificate certificate = certificateMapper.toEntity(certificateService.find(certificateId));
        Order newOrder = Order.builder()
                .user(user)
                .certificate(certificate)
                .totalCost(certificate.getPrice())
                .build();
        return orderMapper.toDTO(orderRepository.save(newOrder));
    }

    @Override
    public Page<OrderDTO> getUserOrders(Integer userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDTO);
    }
}
