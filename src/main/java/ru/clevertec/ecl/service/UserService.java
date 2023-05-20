package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.api.IUserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    /**
     * Finds user by id.
     *
     * @param id id of desired user
     * @return found user
     * @throws EntityNotFoundException if user not found
     */
    @Override
    public User find(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with such id not found", id));
    }

    /**
     * Finds all users with pagination.
     *
     * @param pageable pageable
     * @return page with users info
     */
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
