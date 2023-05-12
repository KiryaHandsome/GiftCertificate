package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagRequestMapper;
import ru.clevertec.ecl.mapper.TagResponseMapper;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.api.CrudService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService implements CrudService<TagRequest, TagResponse, Integer> {

    private final TagRepository tagRepository;
    private final TagResponseMapper responseMapper = Mappers.getMapper(TagResponseMapper.class);
    private final TagRequestMapper requestMapper = Mappers.getMapper(TagRequestMapper.class);

    /**
     * Finds all tags.
     *
     * @return list of tags
     */
    @Override
    public Page<TagResponse> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(responseMapper::toDTO);
    }

    /**
     * Deletes tag by id.
     *
     * @param id id of tag to delete
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }

    /**
     * Creates new tag.
     *
     * @param tagRequest tag entity with name
     * @return tag with created id
     */
    @Override
    @Transactional
    public TagResponse save(TagRequest tagRequest) {
        // Do I need this?
        tagRepository.findByName(tagRequest.getName())
                .ifPresent(t -> {
                    throw new IllegalArgumentException("Tag with such name already exists");
                });
        Tag tag = requestMapper.toEntity(tagRequest);
        Tag savedTag = tagRepository.save(tag);
        return responseMapper.toDTO(savedTag);
    }

    /**
     * Updates tag.
     *
     * @param id         id of updatable tag
     * @param tagRequest tag with new name
     * @return updated tag
     */
    @Override
    @Transactional
    public TagResponse update(Integer id, TagRequest tagRequest) {
        Tag tag = requestMapper.toEntity(tagRequest);
        tag.setId(id);
        Tag updatedTag = tagRepository.save(tag);
        return responseMapper.toDTO(updatedTag);
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
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found", id));
        return responseMapper.toDTO(tag);
    }

    public List<Tag> findAllByNameIn(List<String> names) {
        return tagRepository.findAllByNameIn(names);
    }
}
