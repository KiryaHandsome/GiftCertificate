package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.net.URI;

@RestController
@RequestMapping(value = "/gift-certificates")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService certificateService;

    /**
     * Endpoint for getting all certificates considering passed parameters.
     * All parameters are optional and can be used in conjunction.
     * <p>
     * Examples of url:
     * {@code /gift-certificates?tag-name=anyName&sort-by-date=asc&sort-by-name=desc&description=beauty&page=10&size=5}
     *
     * @param tagName     tag name. If it is presented then endpoint returns list of certificates
     *                    which contains tag with such name.
     * @param description part of description in desired certificates. If it's passed endpoint will return certificates
     *                    which contain passed description as substring. Case-insensitive
     * @return list of found gift certificates
     */
    @GetMapping
    public ResponseEntity<Page<GiftCertificateResponse>> getAllCertificates(
            @RequestParam(required = false, name = "tag-name") String tagName,
            @RequestParam(required = false, name = "description") String description,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<GiftCertificateResponse> certificates = certificateService
                .findAll(
                        tagName,
                        description,
                        pageable
                );
        return ResponseEntity.ok(certificates);
    }

    /**
     * Endpoint for creating new certificate.
     * <p>
     * DTO class that presents request entity: {@link GiftCertificateRequest}
     * Request body example:
     * <pre>
     * {@code {
     *     "name" : "neeeeeww",
     *     "description" : "nails certificate",
     *     "duration" : 1,
     *     "tags": [
     *         { "name" : "nails"},
     *         { "name" : "beauty"}
     *     ]
     * }}
     * <pre/>
     * URL: {@code /gift-certificates}
     * @param certificateDTO request dto
     * @return response with location of created certificate
     */
    @PostMapping
    public ResponseEntity<Void> createCertificate(
            @RequestBody
            GiftCertificateRequest certificateDTO
    ) {
        GiftCertificateResponse createdCertificate = certificateService.save(certificateDTO);
        return ResponseEntity
                .created(URI.create("/gift-certificates/" + createdCertificate.getId()))
                .build();
    }

    /**
     * Endpoint for getting certificate by id.
     * <p>
     * URL: {@code /gift-certificates/{id}}
     *
     * @param id desired certificate id
     * @return certificate dto
     * @throws RuntimeException when object not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> getCertificateById(@PathVariable Integer id) {
        GiftCertificateResponse certificateDTO = certificateService.find(id);
        return ResponseEntity.ok(certificateDTO);
    }

    /**
     * Endpoint for deleting certificate by id
     * <p>
     * URL: {@code /gift-certificates/{id}}
     *
     * @param id id of certificate to delete
     * @return 204 code(NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Integer id) {
        certificateService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Endpoint for partial updating of certificate by id.
     * <p>
     * Updates only fields that are passed.
     * Request body example:
     * <pre>
     * {@code {
     *     "duration" : 12,
     *     "tags": [
     *         {"name" : "new tag"}
     *     ]
     * }}
     * <pre/>
     * URL: {@code /gift-certificates/{id}}
     *
     * @param id id of certificate to update
     * @param certificateRequestDTO request dto
     * @return updated certificate
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> updateCertificate(
            @PathVariable Integer id,
            @RequestBody GiftCertificateRequest certificateRequestDTO) {
        GiftCertificateResponse certificateDTO = certificateService.update(id, certificateRequestDTO);
        return ResponseEntity.ok(certificateDTO);
    }
}
