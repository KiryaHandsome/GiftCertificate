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
import ru.clevertec.ecl.exception.InvalidOrderException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(DatabaseConfig.class)
class GiftCertificateRepositoryIntegrationTest {


    private GiftCertificateRepository repository;
    @Autowired
    private SessionFactory sessionFactory;
    private GiftCertificate certificate;

    @BeforeEach
    void setUp() {
        repository = new GiftCertificateRepository(sessionFactory);
        ZonedDateTime time = ZonedDateTime.now();
        List<Tag> tags = List.of(
                new Tag(null, "tag1"),
                new Tag(null, "tag2"),
                new Tag(null, "tag3"),
                new Tag(null, "tag4"));
        certificate = GiftCertificate.builder()
                .id(1)
                .description("cool certificate")
                .name("name")
                .duration(123)
                .createDate(time)
                .lastUpdateDate(time)
                .tags(tags)
                .build();

    }

    @Test
    void checkDelete() {
        GiftCertificate createdCertificate = repository.save(certificate);
        assertThat(createdCertificate).isNotNull();

        Integer id = createdCertificate.getId();
        assertThat(id).isNotNull();

        repository.delete(id);
        Optional<GiftCertificate> certificateOptional = repository.find(id);
        assertThat(certificateOptional).isNotPresent();
    }

    @Test
    void checkSaveShouldGenerateId() {
        GiftCertificate createdCertificate = repository.save(certificate);
        assertThat(createdCertificate).isNotNull();
        assertThat(createdCertificate.getId()).isGreaterThan(0);
    }

    @Test
    void checkUpdateShouldReturnEntityWithUpdatedFields() {
        Integer id = 2;
        String newName = "someNewNameForCertificate";
        String newDescription = "newDescription";
        int newDuration = 10100110;
        GiftCertificate newCertificate = GiftCertificate.builder()
                .name(newName)
                .description(newDescription)
                .duration(newDuration)
                .build();
        GiftCertificate updatedCertificate = repository.update(id, newCertificate);
        assertThat(updatedCertificate.getName()).isEqualTo(newName);
        assertThat(updatedCertificate.getDuration()).isEqualTo(newDuration);
        assertThat(updatedCertificate.getDescription()).isEqualTo(newDescription);
    }

    @ParameterizedTest
    @ValueSource(ints = {100, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void checkUpdateShouldThrowEntityNotFoundException(int id) {
        assertThrows(EntityNotFoundException.class,
                () -> repository.update(id, new GiftCertificate()));
    }


    @Test
    void checkFindAllShouldBeNotEmpty() {
        List<GiftCertificate> result = repository.findAll();
        assertThat(result).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 1000, Integer.MAX_VALUE})
    void checkFindShouldReturnEmptyOptional(int id) {
        Optional<GiftCertificate> result = repository.find(id);
        assertThat(result).isNotPresent();
    }

    @Test
    void checkFindAllWithParamsShouldReturnTagsSortedByDateAsc() {
        String tagName = null;
        String sortByDate = "asc";
        String sortByName = null;
        String description = null;
        List<GiftCertificate> sortedCertificates = repository.findAll();
        sortedCertificates.sort(Comparator.comparing(GiftCertificate::getCreateDate));

        List<GiftCertificate> certificates = repository.findAll(tagName, sortByDate, sortByName, description);

        assertThat(certificates).isEqualTo(sortedCertificates);

    }

    @Test
    void checkFindAllWithParamsShouldThrowInvalidOrderException() {
        String tagName = null;
        String sortByDate = "descc";
        String sortByName = "ascc";
        String description = null;

        assertThrows(InvalidOrderException.class,
                () -> repository.findAll(tagName, sortByDate, sortByName, description));
    }
}