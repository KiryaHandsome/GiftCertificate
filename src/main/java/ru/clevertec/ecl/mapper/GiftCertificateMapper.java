package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;

@Mapper
public interface GiftCertificateMapper {

    GiftCertificateResponse toResponse(GiftCertificate tag);

    GiftCertificateRequest toRequest(GiftCertificate tag);

    GiftCertificate toEntity(GiftCertificateRequest request);

    GiftCertificate toEntity(GiftCertificateResponse response);
}
