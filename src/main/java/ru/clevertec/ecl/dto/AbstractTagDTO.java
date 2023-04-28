package ru.clevertec.ecl.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public abstract class AbstractTagDTO {

    protected String name;
}
