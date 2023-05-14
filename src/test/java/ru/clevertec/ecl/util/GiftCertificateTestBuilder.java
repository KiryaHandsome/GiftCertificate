package ru.clevertec.ecl.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;

import java.time.ZonedDateTime;
import java.util.List;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateTestBuilder {

    private Integer id = 10;
    private String name = "Some name";
    private String description = "Description";
    private Integer duration = 9;
    private Double price = 90.12;
    private ZonedDateTime createDate = ZonedDateTime.now();
    private ZonedDateTime lastUpdateDate = ZonedDateTime.now();
    private List<Tag> tags = List.of(
            new Tag(1, "name1"),
            new Tag(2, "name2"),
            new Tag(3, "some name 3")
    );

    public GiftCertificate build() {
        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .duration(duration)
                .price(price)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(tags)
                .build();
    }
}
