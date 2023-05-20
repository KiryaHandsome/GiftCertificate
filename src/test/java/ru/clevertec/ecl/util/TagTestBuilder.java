package ru.clevertec.ecl.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.model.Tag;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagTestBuilder {

    private Integer id = 10;
    private String name = "SomeTagName";

    public Tag build() {
        return Tag.builder()
                .id(id)
                .name(name)
                .build();
    }

}
