package ru.clevertec.ecl.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.order.OrderRequest;
import ru.clevertec.ecl.dto.order.OrderResponse;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    /**
     * Finds user by id.
     *
     * @param id id of desired user
     * @return found user
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        User user = userService.find(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Finds all users
     *
     * @return page with users
     */
    @GetMapping
    public ResponseEntity<Page<User>> findAll(
            @PageableDefault(size = 20)
            Pageable pageable
    ) {
        Page<User> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Makes order for user.
     *
     * @param userId       user id
     * @param orderRequest dto with id of bought gift certificate
     * @return order dto
     */
    @PostMapping("/{user_id}/orders")
    public ResponseEntity<OrderResponse> makeOrder(
            @PathVariable("user_id") Integer userId,
            @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.makeOrder(userId, orderRequest.getCertificateId());
        return ResponseEntity.ok(order);
    }

    /**
     * Finds user orders by user id.
     *
     * @param userId user id
     * @param pageable pageable
     * @return page of order dto
     */
    @GetMapping("/{user_id}/orders")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @PathVariable("user_id") Integer userId,
            Pageable pageable) {
        Page<OrderResponse> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }
}