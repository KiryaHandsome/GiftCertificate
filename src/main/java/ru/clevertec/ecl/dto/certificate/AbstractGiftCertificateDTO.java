package ru.clevertec.ecl.dto.certificate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.clevertec.ecl.model.Tag;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractGiftCertificateDTO {

    protected String name;
    protected Double price;
    protected String description;
    protected Integer duration;
    protected List<Tag> tags = new ArrayList<>();
}
