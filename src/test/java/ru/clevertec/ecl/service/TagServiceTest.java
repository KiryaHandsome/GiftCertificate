package ru.clevertec.ecl.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dto.TagRequest;
import ru.clevertec.ecl.dto.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagRequestMapper;
import ru.clevertec.ecl.mapper.TagResponseMapper;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;
    @Mock
    private TagRepository mockRepository;

    private Tag tag;
    private TagRequestMapper requestMapper;
    private TagResponseMapper responseMapper;

    @BeforeEach
    void setUp() {
        String name = "some name";
        int id = 1;
        tag = new Tag(id, name);
        responseMapper = Mappers.getMapper(TagResponseMapper.class);
        requestMapper = Mappers.getMapper(TagRequestMapper.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 100, Integer.MAX_VALUE})
    void checkDeleteShouldCallRepositoryWithSameId(int id) {
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(mockRepository).delete(id);
        tagService.delete(id);

        verify(mockRepository).delete(idCaptor.capture());
        Integer actualId = idCaptor.getValue();
        assertThat(actualId).isEqualTo(id);
    }

    @Test
    void checkSaveShouldReturnSameTagResponseAsRepository() {
        String name = "someName";
        Tag tagWithoutId = new Tag(null, name);
        Tag tagWithId = new Tag(1, name);
        TagRequest request = requestMapper.toDTO(tagWithoutId);
        TagResponse response = responseMapper.toDTO(tagWithId);

        when(mockRepository.save(tagWithoutId))
                .thenReturn(tagWithId);
        TagResponse actualResponse = tagService.save(request);

        verify(mockRepository).save(any());
        assertThat(actualResponse).isEqualTo(response);
    }

    @Test
    void checkUpdateShouldReturnExpectedResponse() {
        Integer id = 1;
        tag.setId(null);
        TagResponse expectedResponse = responseMapper.toDTO(tag);

        when(mockRepository.update(id, tag))
                .thenReturn(tag);
        TagResponse actualResponse = tagService.update(id, requestMapper.toDTO(tag));

        verify(mockRepository).update(id, tag);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void checkFindAllShouldReturnSameListAsRepository() {
        List<Tag> tags = List.of(tag);
        List<TagResponse> expectedResponse = List.of(responseMapper.toDTO(tag));

        when(mockRepository.findAll())
                .thenReturn(tags);
        List<TagResponse> actualResponse = tagService.findAll();

        verify(mockRepository).findAll();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void checkFindShouldReturnSameResponse() {
        int id = tag.getId();
        TagResponse expectedResponse = responseMapper.toDTO(tag);

        when(mockRepository.find(id))
                .thenReturn(Optional.of(tag));
        TagResponse actualResponse = tagService.find(id);

        verify(mockRepository).find(id);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void checkFindShouldThrowEntityNotFoundException() {
        Integer id = 1;

        when(mockRepository.find(id))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> tagService.find(id));
    }
}