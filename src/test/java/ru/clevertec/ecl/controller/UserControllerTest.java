package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.OrderMapperImpl;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.OrderTestBuilder;
import ru.clevertec.ecl.util.TestData;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String USER_PATH = "/users/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;
    private OrderMapper orderMapper;
    private OrderTestBuilder ORDER_TEST_BUILDER;


    @BeforeEach
    void setUp() {
        orderMapper = (OrderMapper) new OrderMapperImpl();
        ORDER_TEST_BUILDER = new OrderTestBuilder();
    }

    @Nested
    class GetUserOrdersEndpoint {

        @Test
        void checkGetUserOrdersShouldReturnExpectedPage() throws Exception {
            int userId = 1;
            var order = ORDER_TEST_BUILDER.build();
            var pageable = PageRequest.of(0, 20);
            var expected = new PageImpl<>(List.of(orderMapper.toResponse(order)));

            doReturn(expected)
                    .when(orderService).getUserOrders(userId, pageable);

            mockMvc.perform(get(URI.create(USER_PATH + userId + "/orders"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));

            verify(orderService).getUserOrders(userId, pageable);
        }
    }

    @Nested
    class FindUserByIdEndpointTest {

        @Test
        void checkFindShouldReturnExpectedUser() throws Exception {
            int userId = 1;
            User expected = new User(userId, "name");

            doReturn(expected)
                    .when(userService).find(userId);

            mockMvc.perform(get(URI.create(USER_PATH + userId))
                    .content(objectMapper.writeValueAsString(expected)));
        }

        @Test
        void checkFindShouldThrowEntityNotFoundException() throws Exception {
            int userId = 1;

            doThrow(EntityNotFoundException.class)
                    .when(userService).find(userId);

            mockMvc.perform(get(URI.create(USER_PATH + userId)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class FindAllEndpointTest {

        @Test
        void checkFindAllShouldReturnExpectedPage() throws Exception {
            var expected = TestData.defaultPageWithUsers();

            doReturn(expected)
                    .when(userService).findAll(TestData.defaultPageable());

            mockMvc.perform(get(URI.create(USER_PATH))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));

            verify(userService).findAll(TestData.defaultPageable());
        }
    }
}
