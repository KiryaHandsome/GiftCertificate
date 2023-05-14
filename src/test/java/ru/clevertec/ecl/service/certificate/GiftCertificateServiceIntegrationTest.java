package ru.clevertec.ecl.service.certificate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.service.AbstractIntegrationTest;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.GiftCertificateTestBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
public class GiftCertificateServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GiftCertificateService certificateService;

    @Autowired
    private TagService tagService;

    @Autowired
    private GiftCertificateMapper certificateMapper;

    @Autowired
    private TagMapper tagMapper;
    private GiftCertificateTestBuilder GC_BUILDER;

    @BeforeEach
    void setUp() {
        GC_BUILDER = new GiftCertificateTestBuilder();
    }

    @Nested
    class DeleteTest {

        @Test
        void checkDeleteShouldThrowEntityNotFoundExceptionOnServiceFind() {
            certificateService.delete(1);
            assertThrows(EntityNotFoundException.class,
                    () -> certificateService.find(1));
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, -1})
        void checkDeleteShouldDoNothing(int id) {
            certificateService.delete(id);
        }
    }

    @Nested
    class FindByIdTest {

        @Test
        void checkFindByIdShouldReturnEntity() {
            GiftCertificateResponse response = certificateService.find(1);
            assertThat(response).isNotNull();
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, -1})
        void checkFindByIdShouldThrowsEntityNotFoundException() {
            GiftCertificateResponse response = certificateService.find(1);
            assertThat(response).isNotNull();
        }
    }

    @Nested
    class FindAllTest {

        @Test
        void checkFindAllShouldReturnNotEmptyList() {
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);
            Page<GiftCertificateResponse> all = certificateService.findAll(pageable);
            assertThat(all).isNotEmpty();
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void checkUpdateShouldReturnUpdatedEntity() {
            Integer id = 2;
            GiftCertificate certificate = GC_BUILDER.withId(id).build();
            GiftCertificateRequest request = certificateMapper.toRequest(certificate);
            GiftCertificateResponse expected = certificateMapper.toResponse(certificate);
            expected.setId(id);

            certificateService.update(id, request);
            var actual = certificateService.find(id);

            compareExceptTime(actual, expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, -1})
        void checkUpdateShouldThrowEntityNotFoundException(int id) {
            assertThrows(EntityNotFoundException.class,
                    () -> certificateService.update(id, null));
        }
    }

    @Nested
    class SaveTest {

        @Test
        void checkSaveShouldReturnEntityWithCreatedIds() {
            GiftCertificate certificate = GC_BUILDER.build();
            certificate.setId(null);
            GiftCertificateRequest request = certificateMapper.toRequest(certificate);

            var actual = certificateService.save(request);

            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isNotNull();
            List<Tag> tags = actual.getTags();
            assertThat(tags).isNotNull();
            assertThat(tags).isNotEmpty();
            for (Tag tag : tags) {
                assertThat(tag.getId()).isNotNull();
            }
        }

        @Test
        void checkSaveWithExistingTagsShould() {
            GiftCertificate certificate = GC_BUILDER.withId(null).build();
            TagRequest[] newTags = {
                    new TagRequest("new Tag1"),
                    new TagRequest("new Tag2"),
                    new TagRequest("new Tag3")};
            List<Tag> tags = new ArrayList<>();
            for (TagRequest tag : newTags) {
                TagResponse createdTagResponse = tagService.save(tag);
                Tag createdEntity = tagMapper.toEntity(createdTagResponse);
                tags.add(createdEntity);
            }
            certificate.setTags(tags);
            GiftCertificateRequest request = certificateMapper.toRequest(certificate);

            var actual = certificateService.save(request);

            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getTags()).isNotNull();
            assertThat(actual.getTags()).isNotEmpty();
            for (Tag tag : actual.getTags()) {
                assertThat(tag.getId()).isNotNull();
            }
        }
    }

    @Nested
    class FindAllWithParams {
        private static final int page = 0;
        private static final int size = 10;

        @Test
        void checkFindAllWithParamsShouldReturnNotEmptyList() {
            Pageable pageable = PageRequest.of(page, size);
            Page<GiftCertificateResponse> response = certificateService
                    .findAll(null, null, pageable);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4})
        void checkFindAllWithParamsShouldReturnEmptyList(int pageNumber) {
            Pageable pageable = PageRequest.of(pageNumber, size);
            Page<GiftCertificateResponse> response = certificateService
                    .findAll(null, null, pageable);
            assertThat(response).isNotNull();
            assertThat(response).isEmpty();
        }

        @Test
        void checkFindAllWithParamsShouldBeSortedInAscendingOrderByCreateDate() {
            Sort sortByCreateDate = Sort.by(Sort.Direction.ASC, "createDate");
            Pageable pageable = PageRequest.of(page, size, sortByCreateDate);
            Page<GiftCertificateResponse> response = certificateService
                    .findAll(null, null, pageable);
            assertThat(response.getContent()).isNotNull();
            assertThat(response.getContent()).isNotEmpty();
            assertThat(response.getContent())
                    .isSortedAccordingTo(
                            Comparator.comparing(GiftCertificateResponse::getCreateDate)
                    );
        }

        @Test
        void checkFindAllWithParamsShouldReturnCertificatesWithNeededTag() {
            String tagName = "beauty";
            Pageable pageable = PageRequest.of(page, size);
            Page<GiftCertificateResponse> response = certificateService
                    .findAll(tagName, null, pageable);
            assertThat(response).isNotNull();
            assertThat(response).isNotEmpty();

            for (var certificate : response) {
                Optional<Tag> tag = certificate.getTags()
                        .stream()
                        .filter(t -> tagName.equals(t.getName()))
                        .findFirst();
                assertThat(tag).isPresent();
            }
        }
    }

    private void compareExceptTime(GiftCertificateResponse actual, GiftCertificateResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getTags()).isEqualTo(expected.getTags());
    }
}
