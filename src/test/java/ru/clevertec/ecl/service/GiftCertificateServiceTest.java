package ru.clevertec.ecl.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dto.GiftCertificateRequest;
import ru.clevertec.ecl.dto.GiftCertificateResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.InvalidOrderException;
import ru.clevertec.ecl.mapper.GiftCertificateRequestMapper;
import ru.clevertec.ecl.mapper.GiftCertificateResponseMapper;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    @InjectMocks
    private GiftCertificateService certificateService;
    @Mock
    private GiftCertificateRepository mockRepository;

    private GiftCertificate certificate;
    private GiftCertificateRequestMapper requestMapper;
    private GiftCertificateResponseMapper responseMapper;


    @BeforeEach
    void setUp() {
        ZonedDateTime time = ZonedDateTime.now();
        List<Tag> tags = List.of(
                new Tag(1, "tag1"),
                new Tag(2, "tag2"),
                new Tag(3, "tag3"),
                new Tag(4, "tag4"));
        certificate = GiftCertificate.builder()
                .id(1)
                .description("cool certificate")
                .name("name")
                .duration(123)
                .createDate(time)
                .lastUpdateDate(time)
                .tags(tags)
                .build();
        responseMapper = Mappers.getMapper(GiftCertificateResponseMapper.class);
        requestMapper = Mappers.getMapper(GiftCertificateRequestMapper.class);
    }


    @Test
    void checkDeleteShouldCallRepositoryWithSameId() {
        Integer id = certificate.getId();
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(mockRepository).delete(id);
        certificateService.delete(id);

        verify(mockRepository).delete(idCaptor.capture());
        Integer actualId = idCaptor.getValue();
        assertThat(actualId).isEqualTo(id);
    }

    @Test
    void checkSave() {
        clearDatesAndId(certificate);
        GiftCertificateRequest request = requestMapper.toDTO(certificate);
        GiftCertificateResponse expectedResponse = responseMapper.toDTO(certificate);

        when(mockRepository.save(certificate))
                .thenReturn(certificate);
        GiftCertificateResponse actualResponse = certificateService.save(request);

        verify(mockRepository).save(certificate);
        assertThat(actualResponse.getDuration()).isEqualTo(expectedResponse.getDuration());
        assertThat(actualResponse.getName()).isEqualTo(expectedResponse.getName());
        assertThat(actualResponse.getDescription()).isEqualTo(expectedResponse.getDescription());
    }

    @Test
    void checkUpdateShouldReturnSameEntity() {
        Integer id = 1;
        clearDatesAndId(certificate);
        GiftCertificateRequest request = requestMapper.toDTO(certificate);
        GiftCertificateResponse expectedResponse = responseMapper.toDTO(certificate);

        when(mockRepository.update(id, certificate))
                .thenReturn(certificate);
        GiftCertificateResponse actualResponse = certificateService.update(id, request);

        verify(mockRepository).update(id, certificate);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void checkUpdateShouldThrowEntityNotFoundException() {
        Integer id = -1;
        clearDatesAndId(certificate);
        GiftCertificateRequest request = requestMapper.toDTO(certificate);

        when(mockRepository.update(id, certificate))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> certificateService.update(id, request));
    }

    @Test
    void checkFindAllShouldReturnSameList() {
        GiftCertificateResponse response = responseMapper.toDTO(certificate);

        when(mockRepository.findAll())
                .thenReturn(List.of(certificate));
        List<GiftCertificateResponse> actualResponses = certificateService.findAll();

        verify(mockRepository).findAll();
        assertThat(actualResponses).isEqualTo(List.of(response));
    }

    @Test
    void checkFindAllWithParamsShouldReturnSameList() {
        String tagName = "some name";
        String description = "description";
        String sortByDate = "asc";
        String sortByName = "desc";
        GiftCertificateResponse expectedResponse = responseMapper.toDTO(certificate);

        when(mockRepository.findAll(tagName, sortByDate, sortByName, description))
                .thenReturn(List.of(certificate));
        List<GiftCertificateResponse> actualResult = certificateService
                .findAll(tagName,
                        sortByDate,
                        sortByName,
                        description);

        verify(mockRepository).findAll(tagName, sortByDate, sortByName, description);
        assertThat(actualResult).isEqualTo(List.of(expectedResponse));
    }

    @Test
    void checkFindAllWithParamsShouldThrowInvalidOrderException() {
        when(mockRepository.findAll(any(), any(), any(), any()))
                .thenThrow(InvalidOrderException.class);

        assertThrows(InvalidOrderException.class,
                () -> certificateService.findAll(any(), any(), any(), any()));
    }

    @Test
    void checkFindShouldCallRepositoryWithSameId() {
        Integer id = 1;
        GiftCertificateResponse expectedResponse = responseMapper.toDTO(certificate);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        when(mockRepository.find(id))
                .thenReturn(Optional.of(certificate));

        GiftCertificateResponse actualResult = certificateService.find(id);

        verify(mockRepository).find(idCaptor.capture());
        assertThat(actualResult).isEqualTo(expectedResponse);
        Integer actualId = idCaptor.getValue();
        assertThat(actualId).isEqualTo(id);
    }

    @Test
    void checkFindShouldThrowEntityNotFoundException() {
        Integer id = 1;

        when(mockRepository.find(id))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> certificateService.find(id));
    }

    private static void clearDatesAndId(GiftCertificate certificate) {
        certificate.setLastUpdateDate(null);
        certificate.setCreateDate(null);
        certificate.setId(null);
    }

}
