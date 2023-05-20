package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.api.CrudService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService implements CrudService<TagRequest, TagResponse, Integer> {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Finds all tags.
     *
     * @return list of tags
     */
    @Override
    public Page<TagResponse> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(tagMapper::toResponse);
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
        Tag tag = tagMapper.toEntity(tagRequest);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toResponse(savedTag);
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
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found", id));
        tag.setName(tagRequest.getName());
        Tag updatedTag = tagRepository.save(tag);
        return tagMapper.toResponse(updatedTag);
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
        return tagMapper.toResponse(tag);
    }

    public List<Tag> findAllByNameIn(List<String> names) {
        return tagRepository.findAllByNameIn(names);
    }
}
