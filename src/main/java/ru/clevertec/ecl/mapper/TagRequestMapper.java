package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.mapper.api.DTOMapper;
import ru.clevertec.ecl.model.Tag;

@Mapper
public interface TagRequestMapper extends DTOMapper<Tag, TagRequest> {

}
