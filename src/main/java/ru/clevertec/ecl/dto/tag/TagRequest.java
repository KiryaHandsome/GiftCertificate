package ru.clevertec.ecl.dto.tag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class TagRequest extends AbstractTagDTO {

    public TagRequest(String name) {
        super(name);
    }
}
