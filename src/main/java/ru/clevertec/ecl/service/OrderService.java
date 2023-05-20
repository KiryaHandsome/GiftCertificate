package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.order.OrderResponse;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
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

    private final UserService userService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final GiftCertificateMapper certificateMapper;
    private final GiftCertificateService certificateService;

    @Override
    @Transactional
    public OrderResponse makeOrder(Integer userId, Integer certificateId) {
        User user = userService.find(userId);
        GiftCertificateResponse certificateResponse = certificateService.find(certificateId);
        GiftCertificate certificate = certificateMapper.toEntity(certificateResponse);
        Order newOrder = Order.builder()
                .user(user)
                .certificate(certificate)
                .totalCost(certificate.getPrice())
                .build();
        return orderMapper.toResponse(orderRepository.save(newOrder));
    }

    @Override
    public Page<OrderResponse> getUserOrders(Integer userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toResponse);
    }
}
