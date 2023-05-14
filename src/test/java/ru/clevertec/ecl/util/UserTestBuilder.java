package ru.clevertec.ecl.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.model.User;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestBuilder {

    private int id = 10;
    private String name = "Some name";

    public User build() {
        return User.builder()
                .id(id)
                .name(name)
                .build();
    }

}
