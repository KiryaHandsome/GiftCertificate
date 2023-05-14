package ru.clevertec.ecl.dto.certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.clevertec.ecl.model.Tag;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double price;
    private String description;

    @Positive
    private Integer duration;
    private List<Tag> tags = new ArrayList<>();
}
