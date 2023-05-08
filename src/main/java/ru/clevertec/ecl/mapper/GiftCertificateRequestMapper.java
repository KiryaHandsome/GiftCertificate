package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.GiftCertificateRequest;
import ru.clevertec.ecl.mapper.api.DTOMapper;
import ru.clevertec.ecl.model.GiftCertificate;

@Mapper(componentModel = "spring")
public interface GiftCertificateRequestMapper extends DTOMapper<GiftCertificate, GiftCertificateRequest> {

}
