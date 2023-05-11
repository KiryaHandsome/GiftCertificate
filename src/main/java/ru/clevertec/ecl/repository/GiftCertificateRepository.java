package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.model.GiftCertificate;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer> {

    @Query("""
            SELECT gc FROM GiftCertificate gc
            JOIN FETCH gc.tags t
            JOIN gc.tags
            WHERE (:tag_name IS NULL OR t.name = :tag_name)
            OR (:description IS NULL OR gc.description LIKE %:description%)
            """)
    List<GiftCertificate> findAll(@Param("tag_name") String tagName,
                                  @Param("description") String description,
                                  Pageable pageable);
}
