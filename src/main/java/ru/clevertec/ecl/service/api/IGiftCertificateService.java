package ru.clevertec.ecl.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;

public interface IGiftCertificateService
        extends CrudService<GiftCertificateRequest, GiftCertificateResponse, Integer> {

    Page<GiftCertificateResponse> findAll(String tagName,
                                          String description,
                                          Pageable pageable);
}
