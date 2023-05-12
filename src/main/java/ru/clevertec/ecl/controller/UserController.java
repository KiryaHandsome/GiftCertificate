package ru.clevertec.ecl.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
     * @return list of users
     */
    @GetMapping
    public ResponseEntity<Page<User>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }
}