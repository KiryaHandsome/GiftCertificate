package ru.clevertec.ecl.service.api;

import ru.clevertec.ecl.dto.GiftCertificateRequest;
import ru.clevertec.ecl.dto.GiftCertificateResponse;

import java.util.List;

public interface IGiftCertificateService
        extends CrudService<GiftCertificateRequest, GiftCertificateResponse, Integer> {

    List<GiftCertificateResponse> findAll(String tagName,
                                          String sortByDate,
                                          String sortByName,
                                          String description);
}
