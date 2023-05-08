package ru.clevertec.ecl.mapper.api;


public interface DTOMapper<E, DTO> {

    DTO toDTO(E entity);

    E toEntity(DTO dto);
}
