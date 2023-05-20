package ru.clevertec.ecl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.order.OrderResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.GiftCertificateMapperImpl;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.OrderMapperImpl;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.GiftCertificateTestBuilder;
import ru.clevertec.ecl.util.OrderTestBuilder;
import ru.clevertec.ecl.util.TestData;
import ru.clevertec.ecl.util.UserTestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;

    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService certificateService;
    private OrderService orderService;
    private GiftCertificateMapper certificateMapper;
    private OrderMapper orderMapper;
    private OrderTestBuilder ORDER_BUILDER;

    @BeforeEach
    void setUp() {
        certificateMapper = (GiftCertificateMapper) new GiftCertificateMapperImpl();
        orderMapper = (OrderMapper) new OrderMapperImpl();
        orderService = new OrderService(
                userService,
                orderMapper,
                mockRepository,
                certificateMapper,
                certificateService
        );
        ORDER_BUILDER = new OrderTestBuilder();
    }

    @Nested
    class MakeOrderTest {

        @Test
        void checkMakeOrderShouldReturnExpectedOrder() {
            int userId = 10;
            int certificateId = 5;
            var user = new UserTestBuilder().build();
            var certificate = new GiftCertificateTestBuilder().withId(certificateId).build();
            var certResponse = certificateMapper.toResponse(certificate);
            var order = Order.builder()
                    .certificate(certificate)
                    .totalCost(certificate.getPrice())
                    .user(user)
                    .build();
            var expected = orderMapper.toResponse(order);

            doReturn(user)
                    .when(userService).find(userId);
            doReturn(certResponse)
                    .when(certificateService).find(certificateId);
            doReturn(order)
                    .when(mockRepository).save(order);

            OrderResponse actual = orderService.makeOrder(userId, certificateId);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkMakeOrderShouldThrowEntityNotFoundExceptionCauseUserService() {
            int userId = 12;

            doThrow(EntityNotFoundException.class)
                    .when(userService).find(userId);

            assertThrows(EntityNotFoundException.class,
                    () -> orderService.makeOrder(userId, null));
        }

        @Test
        void checkMakeOrderShouldThrowEntityNotFoundExceptionCauseCertificateService() {
            int userId = 1;
            int certificateId = 11;

            doReturn(new User())
                    .when(userService).find(userId);

            doThrow(EntityNotFoundException.class)
                    .when(certificateService).find(certificateId);

            assertThrows(EntityNotFoundException.class,
                    () -> orderService.makeOrder(userId, certificateId));
        }
    }

    @Nested
    class GetUserOrdersTest {

        @Test
        void checkGetUserOrdersShouldReturnExpectedPage() {
            int userId = 1;
            Pageable pageable = TestData.defaultPageable();
            Page<Order> orders = new PageImpl<>(List.of(
                    ORDER_BUILDER.build(),
                    ORDER_BUILDER.withTotalCost(199123.12).build(),
                    ORDER_BUILDER.withId(9).build()
            ));
            Page<OrderResponse> expected = orders.map(orderMapper::toResponse);

            doReturn(orders)
                    .when(mockRepository).findAllByUserId(userId, pageable);

            Page<OrderResponse> actual = orderService.getUserOrders(userId, pageable);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkGetUserOrdersShouldReturnEmptyPage() {
            int userId = 1;
            Pageable pageable = TestData.defaultPageable();
            Page<Order> orders = new PageImpl<>(new ArrayList<>());

            doReturn(orders)
                    .when(mockRepository).findAllByUserId(userId, pageable);

            Page<OrderResponse> actual = orderService.getUserOrders(userId, pageable);
            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isEmpty();
        }
    }
}