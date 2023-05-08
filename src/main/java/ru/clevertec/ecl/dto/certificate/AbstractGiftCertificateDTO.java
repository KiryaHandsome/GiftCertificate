package ru.clevertec.ecl.dto.certificate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.clevertec.ecl.model.Tag;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
public abstract class AbstractGiftCertificateDTO {

    protected String name;
    protected String description;
    protected Integer duration;
    protected List<Tag> tags = new ArrayList<>();
}
