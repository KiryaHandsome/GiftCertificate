package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.api.IGiftCertificateService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GiftCertificateService implements IGiftCertificateService {

    private final GiftCertificateMapper certificateMapper;
    private final TagMapper tagMapper;
    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;

    /**
     * Deletes certificate by id
     *
     * @param id id of certificate to delete
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        certificateRepository.deleteById(id);
    }

    /**
     * Creates new certificate
     *
     * @param certificateRequestDTO new certificate
     * @return certificate with created id
     */
    @Override
    @Transactional
    public GiftCertificateResponse save(GiftCertificateRequest certificateRequestDTO) {
        GiftCertificate giftCertificate = certificateMapper.toEntity(certificateRequestDTO);
        List<String> names = certificateRequestDTO.getTags()
                .stream()
                .map(Tag::getName)
                .toList();
        List<Tag> existingTags = tagService.findAllByNameIn(names);
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : certificateRequestDTO.getTags()) {
            tags.add(existingTags
                    .stream()
                    .filter(t -> t.getName().equals(tag.getName()))
                    .findFirst()
                    .orElseGet(() -> tagMapper.toEntity(
                                    tagService.save(new TagRequest(tag.getName()))
                            )
                    )
            );
        }
        giftCertificate.setTags(tags);
        GiftCertificate certificate = certificateRepository.save(giftCertificate);
        return certificateMapper.toResponse(certificate);
    }

    /**
     * Partial update of certificate.
     * Wraps call of(see for more information)
     *
     * @param id      id of updatable certificate
     * @param request entity with updatable fields
     * @return updated certificate
     * @throws EntityNotFoundException if certificate with such id doesn't exist
     */
    @Override
    @Transactional
    public GiftCertificateResponse update(Integer id, GiftCertificateRequest request) {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate with such id not found", id));
        setIfNotNull(request, certificate);
        GiftCertificate updatedCertificate = certificateRepository.save(certificate);
        return certificateMapper.toResponse(updatedCertificate);
    }

    /**
     * Finds all certificates.
     *
     * @return list of certificates responsesDTO
     */
    @Override
    public Page<GiftCertificateResponse> findAll(Pageable pageable) {
        return certificateRepository.findAll(pageable)
                .map(certificateMapper::toResponse);
    }

    /**
     * Finds certificate by id.
     *
     * @param id id of desired certificate
     * @return optional response DTO of found certificate
     * @throws EntityNotFoundException when certificate with such id doesn't exist
     */
    @Override
    public GiftCertificateResponse find(Integer id) {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gift certificate not found", id));
        return certificateMapper.toResponse(certificate);
    }

    @Override
    public Page<GiftCertificateResponse> findAll(
            String tagName,
            String description,
            Pageable pageable
    ) {
        return certificateRepository.findAll(tagName, description, pageable)
                .map(certificateMapper::toResponse);
    }

    private static void setIfNotNull(GiftCertificateRequest source, GiftCertificate destination) {
        if (source.getName() != null)
            destination.setName(source.getName());
        if (source.getDuration() != null)
            destination.setDuration(source.getDuration());
        if (source.getPrice() != null)
            destination.setPrice(source.getPrice());
        if (source.getDescription() != null)
            destination.setDescription(source.getDescription());
        if (source.getTags() != null)
            destination.setTags(source.getTags());
    }
}
