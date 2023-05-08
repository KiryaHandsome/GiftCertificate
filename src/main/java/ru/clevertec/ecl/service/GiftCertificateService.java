package ru.clevertec.ecl.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.GiftCertificateRequest;
import ru.clevertec.ecl.dto.GiftCertificateResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateRequestMapper;
import ru.clevertec.ecl.mapper.GiftCertificateResponseMapper;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.api.IGiftCertificateService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftCertificateService implements IGiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final GiftCertificateResponseMapper responseMapper = Mappers.getMapper(GiftCertificateResponseMapper.class);
    private final GiftCertificateRequestMapper requestMapper = Mappers.getMapper(GiftCertificateRequestMapper.class);

    /**
     * Deletes certificate by id
     *
     * @param id id of certificate to delete
     */
    @Override
    public void delete(Integer id) {
        certificateRepository.delete(id);
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
        GiftCertificate certificate = certificateRepository.save(giftCertificate);
        return responseMapper.toDTO(certificate);
    }

    /**
     * Partial update of certificate.
     * Wraps call of(see for more information)
     * {@link ru.clevertec.ecl.repository.GiftCertificateRepository#update(Integer, GiftCertificate)}
     *
     * @param id             id of updatable certificate
     * @param certificateDTO entity with updatable fields
     * @return updated certificate
     */
    @Override
    public GiftCertificateResponse update(Integer id, GiftCertificateRequest certificateDTO) {
        GiftCertificate certificate = requestMapper.toEntity(certificateDTO);
        GiftCertificate updatedCertificate = certificateRepository.update(id, certificate);
        return responseMapper.toDTO(updatedCertificate);
    }

    /**
     * Finds all certificates.
     *
     * @return list of certificates responsesDTO
     */
    @Override
    public List<GiftCertificateResponse> findAll() {
        return certificateRepository.findAll()
                .stream()
                .map(responseMapper::toDTO)
                .toList();
    }

    /**
     * Finds certificate by id.
     * See {@link ru.clevertec.ecl.repository.GiftCertificateRepository#find(Integer)}
     *
     * @param id id of desired certificate
     * @return optional response DTO of found certificate
     * @throws EntityNotFoundException when certificate with such id doesn't exist
     */
    @Override
    public GiftCertificateResponse find(Integer id) {
        GiftCertificate certificate = certificateRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException("Gift certificate not found", id));
        return responseMapper.toDTO(certificate);
    }

    /**
     * Finds all tags and converts them to list of
     * {@link GiftCertificateResponse}. <br/>
     * Wraps call of(see for more information)
     * {@link ru.clevertec.ecl.repository.GiftCertificateRepository#findAll(String, String, String, String)}
     */
    @Override
    public List<GiftCertificateResponse> findAll(String tagName,
                                                 String sortByDate,
                                                 String sortByName,
                                                 String description) {
        return certificateRepository.findAll(
                        tagName,
                        sortByDate,
                        sortByName,
                        description)
                .stream()
                .map(responseMapper::toDTO)
                .toList();
    }
}
