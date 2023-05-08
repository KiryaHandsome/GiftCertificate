package ru.clevertec.ecl.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.ecl.config.DatabaseConfig;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.Tag;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringJUnitConfig(DatabaseConfig.class)
class TagRepositoryIntegrationTest {

    private TagRepository repository;
    @Autowired
    private SessionFactory sessionFactory;
    private Tag tag;

    @BeforeEach
    void setUp() {
        repository = new TagRepository(sessionFactory);
        tag = new Tag(null, "some name");
    }

    @Test
    void checkDelete() {
        Tag createdTag = repository.save(tag);
        assertThat(createdTag).isNotNull();

        Integer id = createdTag.getId();
        assertThat(id).isNotNull();

        repository.delete(id);
        Optional<Tag> tagOptional = repository.find(id);
        assertThat(tagOptional).isNotPresent();
    }

    @Test
    void checkDeleteShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> repository.delete(Integer.MIN_VALUE));
    }

    @Test
    void checkSaveShouldGenerateId() {
        Tag tagToSave = new Tag(null, "some name");

        Tag createdTag = repository.save(tagToSave);
        assertThat(createdTag).isNotNull();
        assertThat(createdTag.getId()).isGreaterThan(0);
    }

    @Test
    void checkUpdateShouldSetNewName() {
        Integer id = 1;
        String newName = "SOME NEW NAME";
        Optional<Tag> oldTagOptional = repository.find(id);
        assertThat(oldTagOptional).isPresent();
        Tag oldTag = oldTagOptional.get();
        assertThat(oldTag.getName()).isNotEqualTo(newName);

        Tag updatedTag = new Tag(null, newName);
        updatedTag = repository.update(id, updatedTag);

        assertThat(updatedTag).isNotNull();
        assertThat(updatedTag.getName()).isEqualTo(newName);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 100, Integer.MAX_VALUE})
    void checkUpdateShouldThrowEntityNotFoundException(int id) {
        assertThrows(EntityNotFoundException.class,
                () -> repository.update(id, tag));
    }

    @Test
    void checkFindAll() {
        List<Tag> result = repository.findAll();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    void checkFindShouldReturnExpectedTag() {
        Integer id = 3;
        Tag expectedTag = new Tag(3, "sport");

        Optional<Tag> optionalTag = repository.find(id);

        assertThat(optionalTag).isPresent();
        assertThat(optionalTag.get()).isEqualTo(expectedTag);
    }

    @Test
    void checkFindShouldReturnEmptyOptional() {
        Integer id = Integer.MIN_VALUE;

        Optional<Tag> optionalTag = repository.find(id);

        assertThat(optionalTag).isNotPresent();
    }
}