package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateRequestMapper;
import ru.clevertec.ecl.mapper.GiftCertificateResponseMapper;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.api.IGiftCertificateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class GiftCertificateService implements IGiftCertificateService {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final GiftCertificateResponseMapper responseMapper = Mappers.getMapper(GiftCertificateResponseMapper.class);
    private static final GiftCertificateRequestMapper requestMapper = Mappers.getMapper(GiftCertificateRequestMapper.class);

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    /**
     * Deletes certificate by id
     *
     * @param id id of certificate to delete
     */
    @Override
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
    public GiftCertificateResponse save(GiftCertificateRequest certificateRequestDTO) {
        GiftCertificate giftCertificate = requestMapper.toEntity(certificateRequestDTO);
        List<String> names = certificateRequestDTO.getTags()
                .stream()
                .map(Tag::getName)
                .toList();
        List<Tag> existingTags = tagRepository.findAllByNameIn(names);
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : certificateRequestDTO.getTags()) {
            tags.add(existingTags
                    .stream()
                    .filter(t -> t.getName().equals(tag.getName()))
                    .findFirst()
                    .orElseGet(() -> tagRepository.save(new Tag(null, tag.getName()))));
        }
        giftCertificate.setTags(tags);
        GiftCertificate certificate = certificateRepository.save(giftCertificate);
        return responseMapper.toDTO(certificate);
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
    public GiftCertificateResponse update(Integer id, GiftCertificateRequest request) {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate with not found", id));
        certificate.setDuration(request.getDuration() != null ? request.getDuration() : certificate.getDuration());
        setIfNotNull(request, certificate);
        GiftCertificate updatedCertificate = certificateRepository.save(certificate);
        return responseMapper.toDTO(updatedCertificate);
    }

    /**
     * Finds all certificates.
     *
     * @return list of certificates responsesDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateResponse> findAll(Integer page, Integer size) {
        return certificateRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    /**
     * Finds certificate by id.
     *
     * @param id id of desired certificate
     * @return optional response DTO of found certificate
     * @throws EntityNotFoundException when certificate with such id doesn't exist
     */
    @Override
    @Transactional(readOnly = true)
    public GiftCertificateResponse find(Integer id) {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gift certificate not found", id));
        return responseMapper.toDTO(certificate);
    }

    @Override
    public List<GiftCertificateResponse> findAll(
            String tagName,
            String description,
            String sortByCreateDate,
            Integer page,
            Integer size
    ) {
        Pageable pageable = createPageable(sortByCreateDate, page, size);
        return certificateRepository
                .findAll(tagName,
                        description,
                        pageable)
                .stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    private static Pageable createPageable(String sortByCreateDate, Integer page, Integer size) {
        int pageNumber = Objects.requireNonNullElse(page, DEFAULT_PAGE_NUMBER);
        int pageSize = Objects.requireNonNullElse(size, DEFAULT_PAGE_SIZE);
        if (sortByCreateDate != null) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortByCreateDate), "createDate");
            return PageRequest.of(pageNumber, pageSize, sort);
        }
        return PageRequest.of(pageNumber, pageSize);
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
