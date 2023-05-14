package ru.clevertec.ecl.service.order;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.order.OrderResponse;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.AbstractIntegrationTest;
import ru.clevertec.ecl.service.OrderService;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class OrderServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    class MakeOrderTest {

        @Test
        void checkMakeOrderShouldReturnCreatedOrder() {
            int userId = 1;
            int certificateId = 3;

            OrderResponse actual = orderService.makeOrder(userId, certificateId);

            assertThat(actual).isNotNull();
            assertThat(actual.getPurchaseDate()).isNotNull();
            assertThat(actual.getTotalCost()).isNotNull();
        }
    }

    @Nested
    class GetUserOrdersTest {

        @Test
        void CheckGetUserOrdersShouldReturnOrderWithSameCost() {
            double expectedCost = 55.32;

            OrderResponse actual = orderService.getUserOrders(1, PageRequest.of(0, 10))
                    .getContent()
                    .get(0);

            assertThat(actual).isNotNull();
            assertThat(actual.getTotalCost()).isEqualTo(expectedCost);
        }

        @Test
        void CheckGetUserOrdersShouldReturnEmptyPage() {
            int userId = Integer.MAX_VALUE;

            Page<OrderResponse> actual = orderService.getUserOrders(userId, PageRequest.of(0, 1));

            assertThat(actual).isNotNull();
            assertThat(actual.getContent()).isNotNull();
            assertThat(actual.getContent()).isEmpty();
        }
    }
}
