package ru.clevertec.ecl.service.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.mapper.TagMapperImpl;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.TagTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository mockRepository;
    private TagMapper tagMapper;
    private TagService tagService;
    private TagTestBuilder TAG_BUILDER;

    @BeforeEach
    void setUp() {
        tagMapper = (TagMapper) new TagMapperImpl();
        tagService = new TagService(mockRepository, tagMapper);
        TAG_BUILDER = new TagTestBuilder();
    }

    @Nested
    class DeleteTest {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 100, Integer.MAX_VALUE})
        void checkDeleteShouldCallRepositoryWithSameId(int id) {
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            doNothing()
                    .when(mockRepository).deleteById(id);

            tagService.delete(id);

            verify(mockRepository).deleteById(idCaptor.capture());
            Integer actualId = idCaptor.getValue();
            assertThat(actualId).isEqualTo(id);
        }
    }

    @Nested
    class SaveTest {

        @Test
        void checkSaveShouldReturnSameTagResponseAsRepository() {
            String name = "someName";
            Tag tagWithoutId = new Tag(null, name);
            Tag tagWithId = new Tag(1, name);
            TagRequest request = tagMapper.toRequest(tagWithoutId);
            TagResponse expected = tagMapper.toResponse(tagWithId);

            doReturn(tagWithId)
                    .when(mockRepository).save(tagWithoutId);
            TagResponse actual = tagService.save(request);

            verify(mockRepository).save(any());
            assertThat(actual).isEqualTo(expected);
        }
    }


    @Test
    void checkUpdateShouldReturnExpectedResponse() {
        Integer id = 1;
        Tag tag = TAG_BUILDER.withId(id).build();
        TagResponse expectedResponse = tagMapper.toResponse(tag);

        doReturn(Optional.of(tag))
                .when(mockRepository).findById(id);
        doReturn(tag)
                .when(mockRepository).save(tag);

        TagResponse actualResponse = tagService.update(id, tagMapper.toRequest(tag));

        verify(mockRepository).save(tag);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void checkFindAllShouldReturnSameListAsRepository() {
        Tag tag = TAG_BUILDER.build();
        Page<Tag> tags = new PageImpl<>(List.of(tag));
        Page<TagResponse> expected = tags.map(tagMapper::toResponse);
        Pageable pageable = PageRequest.of(1, 1);

        doReturn(tags)
                .when(mockRepository).findAll(pageable);

        Page<TagResponse> actual = tagService.findAll(pageable);

        verify(mockRepository).findAll(pageable);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkFindShouldReturnSameResponse() {
        int id = 10;
        Tag tag = TAG_BUILDER.withId(id).build();
        TagResponse expectedResponse = tagMapper.toResponse(tag);

        doReturn(Optional.of(tag))
                .when(mockRepository).findById(id);
        TagResponse actualResponse = tagService.find(id);

        verify(mockRepository).findById(id);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void checkFindShouldThrowEntityNotFoundException() {
        Integer id = 1;

        doReturn(Optional.empty())
                .when(mockRepository).findById(id);

        assertThrows(EntityNotFoundException.class,
                () -> tagService.find(id));
    }
}