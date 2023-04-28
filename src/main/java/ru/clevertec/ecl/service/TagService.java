package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.TagRequest;
import ru.clevertec.ecl.dto.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagRequestMapper;
import ru.clevertec.ecl.mapper.TagResponseMapper;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.api.CrudService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements CrudService<TagRequest, TagResponse, Integer> {

    private final TagRepository tagRepository;
    private final TagResponseMapper responseMapper = Mappers.getMapper(TagResponseMapper.class);
    private final TagRequestMapper requestMapper = Mappers.getMapper(TagRequestMapper.class);;

    /**
     * Deletes tag by id.
     *
     * @param id id of tag to delete
     */
    @Override
    public void delete(Integer id) {
        tagRepository.delete(id);
    }

    /**
     * Creates new tag.
     *
     * @param tagRequest tag entity with name
     * @return tag with created id
     */
    @Override
    public TagResponse save(TagRequest tagRequest) {
        Tag tag = requestMapper.toEntity(tagRequest);
        Tag savedTag = tagRepository.save(tag);
        return responseMapper.toDTO(savedTag);
    }

    /**
     * Updates tag.
     *
     * @param id            id of updatable tag
     * @param tagRequest tag with new name
     * @return updated tag
     */
    @Override
    public TagResponse update(Integer id, TagRequest tagRequest) {
        Tag tag = requestMapper.toEntity(tagRequest);
        Tag updatedTag = tagRepository.update(id, tag);
        return responseMapper.toDTO(updatedTag);
    }

    /**
     * Finds all tags.
     *
     * @return list of tags
     */
    @Override
    public List<TagResponse> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    /**
     * Finds tag by id.
     *
     * @param id id of desired tag
     * @return optional tag
     * @throws EntityNotFoundException when tag with such id doesn't exist
     */
    @Override
    public TagResponse find(Integer id) {
        Tag tag = tagRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found", id));
        return responseMapper.toDTO(tag);
    }
}
