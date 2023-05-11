package ru.clevertec.ecl.service.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;

import java.util.List;

public interface IGiftCertificateService
        extends CrudService<GiftCertificateRequest, GiftCertificateResponse, Integer> {

    List<GiftCertificateResponse> findAll(String tagName,
                                          String description,
                                          String sortByDate,
                                          Integer page,
                                          Integer size);
}
