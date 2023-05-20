package ru.clevertec.ecl.integration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.service.TagService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
public class TagServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagMapper tagMapper;

    @Nested
    class FindAllTest {

        int page = 0;
        int size = 10;

        @Test
        void checkFindAllShouldReturnNotEmptyPage() {
            Pageable pageable = PageRequest.of(page, size);

            Page<TagResponse> actual = tagService.findAll(pageable);

            assertThat(actual).isNotNull();
            assertThat(actual).isNotEmpty();
        }

        @Test
        void checkFindAllShouldReturnPageSortedByName() {
            Pageable pageable = PageRequest.of(page, size);

            Page<TagResponse> actual = tagService.findAll(pageable);

            assertThat(actual).isNotNull();
            assertThat(actual).isNotEmpty();
        }

        @Test
        void checkFindAllShouldReturnEmptyPage() {
            Pageable pageable = PageRequest.of(100, size);

            Page<TagResponse> actual = tagService.findAll(pageable);

            assertThat(actual.getContent()).isEmpty();
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void checkDeleteShouldThrowExceptionOnNextFindCall() {
            int id = 1;
            TagResponse tagResponse = tagService.find(id);
            assertThat(tagResponse).isNotNull();

            tagService.delete(id);

            assertThrows(EntityNotFoundException.class,
                    () -> tagService.find(id));
        }

        @Test
        void checkDeleteShouldDoNothing() {
            Integer id = -1;
            tagService.delete(id);
        }
    }

    @Nested
    class SaveTest {

        @Test
        void checkSaveTagWithNewNameShouldReturnCreatedTag() {
            String name = "Absolutely new name";
            TagRequest tag = tagMapper.toRequest(new Tag(null, name));

            TagResponse actual = tagService.save(tag);

            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isNotNull();
        }

        @Test
        void checkSaveTagWithExistingNameShouldThrowRuntimeException() {
            String name = "beauty";
            TagRequest tag = tagMapper.toRequest(new Tag(null, name));

            assertThrows(RuntimeException.class,
                    () -> tagService.save(tag));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void checkUpdateShouldReturnTagWithUpdatedName() {
            int id = 1;
            String newName = "SomeNewName@!@#$%^&*()_";

            String oldName = tagService.find(id).getName();
            assertThat(oldName).isNotEqualTo(newName);

            TagResponse actual = tagService.update(id, new TagRequest(newName));

            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(id);
            assertThat(actual.getName()).isEqualTo(newName);
        }

        @Test
        void checkUpdateShouldThrowEntityNotFoundException() {
            int id = Integer.MAX_VALUE;

            assertThrows(EntityNotFoundException.class,
                    () -> tagService.update(id, new TagRequest("name")));
        }
    }

    @Nested
    class FindTest {

        @Test
        void checkFindShouldReturnDesiredTag() {
            int id = 2;
            String name = "beauty";
            TagResponse expected = new TagResponse();
            expected.setId(id);
            expected.setName(name);

            TagResponse actual = tagService.find(id);

            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkFindShouldThrowEntityNotFoundException() {
            int id = Integer.MAX_VALUE;

            assertThrows(EntityNotFoundException.class,
                    () -> tagService.find(id));
        }
    }
}
