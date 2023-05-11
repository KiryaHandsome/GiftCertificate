package ru.clevertec.ecl.service.api;

import ru.clevertec.ecl.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    /**
     * Finds user by id
     *
     * @param id id of desired user
     * @return found user
     * @throws ru.clevertec.ecl.exception.EntityNotFoundException when user not found
     */
    User find(Integer id);

    /**
     * Finds all users.
     *
     * @return list of found users
     */
    List<User> findAll(Integer page, Integer size);
}
