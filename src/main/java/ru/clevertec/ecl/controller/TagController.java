package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.TagRequest;
import ru.clevertec.ecl.dto.TagResponse;
import ru.clevertec.ecl.service.TagService;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * Endpoint for getting all tags
     * <p>
     * URL: {@code /tags}
     *
     * @return list of tags
     */
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    /**
     * Endpoint for getting tag by id
     * <p>
     * URL: {@code /tags/{id}}
     *
     * @param id id of desired tag
     * @return tag
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTag(@PathVariable Integer id) {
        TagResponse tag = tagService.find(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Endpoint for creating new tag
     * <p>
     * Request body example:
     * <pre>
     * {@code {
     *     "name": "new tag"
     * }}
     * <pre/>
     * URL: {@code /tags}
     *
     * @param tagRequest new tag
     * @return location to created tag
     */
    @PostMapping
    public ResponseEntity<Void> createTag(@RequestBody TagRequest tagRequest) {
        TagResponse createdTag = tagService.save(tagRequest);
        return ResponseEntity
                .created(URI.create("/tags/" + createdTag.getId()))
                .build();
    }

    /**
     * Endpoint for updating existing tag
     * Request body example:
     * <pre>
     * {@code {
     *     "name": "new tag"
     * }}
     * <pre/>
     * URL: {@code /tags/{id}}
     *
     * @param id id of updatable tag
     * @param tagRequest updated tag
     * @return updated tag
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Integer id,
                                                 @RequestBody TagRequest tagRequest) {
        TagResponse updatedTag = tagService.update(id, tagRequest);
        return ResponseEntity.ok(updatedTag);
    }

    /**
     * Endpoint for deleting tag by id
     * <p>
     * URL: {@code /tags/{id}}
     *
     * @param id id of tag to removing
     * @return 204 code(NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
