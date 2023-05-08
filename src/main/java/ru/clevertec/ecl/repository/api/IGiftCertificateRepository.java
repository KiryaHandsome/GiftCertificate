package ru.clevertec.ecl.repository.api;

import ru.clevertec.ecl.model.GiftCertificate;

import java.util.List;

public interface IGiftCertificateRepository extends CrudRepository<GiftCertificate, Integer> {

    List<GiftCertificate> findAll(String tagName,
                                  String sortByDate,
                                  String sortByName,
                                  String description);
}
