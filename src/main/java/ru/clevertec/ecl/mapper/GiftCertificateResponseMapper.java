package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.mapper.api.DTOMapper;
import ru.clevertec.ecl.model.GiftCertificate;

@Mapper
public interface GiftCertificateResponseMapper extends DTOMapper<GiftCertificate, GiftCertificateResponse> {

}
