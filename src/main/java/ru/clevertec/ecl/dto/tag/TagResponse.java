package ru.clevertec.ecl.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode
public class TagResponse {

    @Positive
    private Integer id;

    @NotBlank
    private String name;
}
