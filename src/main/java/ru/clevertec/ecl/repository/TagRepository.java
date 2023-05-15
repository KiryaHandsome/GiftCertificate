package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Page<Tag> findAll(Pageable pageable);

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNameIn(List<String> names);
}
