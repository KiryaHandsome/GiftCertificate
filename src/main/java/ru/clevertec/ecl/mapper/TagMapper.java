package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.model.Tag;

@Mapper
public interface TagMapper {

    TagResponse toResponse(Tag tag);

    TagRequest toRequest(Tag tag);

    Tag toEntity(TagRequest request);

    Tag toEntity(TagResponse response);
}
