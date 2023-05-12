package ru.clevertec.ecl.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;

public interface IUserService {

    User find(Integer id);

    Page<User> findAll(Pageable pageable);
}
