package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.model.GiftCertificate;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {

    List<GiftCertificate> findAll(String tagName,
                                  String sortByDate,
                                  String sortByName,
                                  String description);
}
