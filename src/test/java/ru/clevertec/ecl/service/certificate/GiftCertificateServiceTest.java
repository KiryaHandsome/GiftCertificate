package ru.clevertec.ecl.service.certificate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.certificate.GiftCertificateResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.GiftCertificateMapperImpl;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.mapper.TagMapperImpl;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.GiftCertificateTestBuilder;
import ru.clevertec.ecl.util.TestData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {


    @Mock
    private TagService mockTagService;

    @Mock
    private GiftCertificateRepository mockRepository;
    private GiftCertificateTestBuilder GC_BUILDER;
    private GiftCertificateMapper certificateMapper;
    private GiftCertificateService certificateService;
    private TagMapper tagMapper;


    @BeforeEach
    void setUp() {
        certificateMapper = (GiftCertificateMapper) new GiftCertificateMapperImpl();
        tagMapper = (TagMapper) new TagMapperImpl();
        certificateService = new GiftCertificateService(
                certificateMapper,
                tagMapper,
                mockRepository,
                mockTagService
        );
        GC_BUILDER = new GiftCertificateTestBuilder();
    }

    @Nested
    class DeleteTest {

        @Test
        void checkDeleteShouldCallRepositoryWithSameId() {
            Integer id = 1;
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            doNothing().when(mockRepository).deleteById(id);
            certificateService.delete(id);

            verify(mockRepository).deleteById(idCaptor.capture());
            Integer actualId = idCaptor.getValue();
            assertThat(actualId).isEqualTo(id);
        }
    }

    @Nested
    class SaveTest {

        @Test
        void checkSaveShouldReturnExpectedResponse() {
            GiftCertificate certificateToSave = TestData.defaultCertificate();
            GiftCertificateRequest request = certificateMapper.toRequest(certificateToSave);
            GiftCertificateResponse expected = certificateMapper.toResponse(certificateToSave);

            doReturn(certificateToSave)
                    .when(mockRepository).save(certificateToSave);
            GiftCertificateResponse actual = certificateService.save(request);

            verify(mockRepository).save(TestData.defaultCertificate());
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void checkUpdateShouldReturnSameEntity() {
            Integer id = 1;
            GiftCertificate certificate  = GC_BUILDER.withId(null).build();
            GiftCertificateRequest request = certificateMapper.toRequest(certificate);
            GiftCertificateResponse expected = certificateMapper.toResponse(certificate);
            expected.setId(id);

            doReturn(Optional.of(certificate))
                    .when(mockRepository).findById(id);
            doReturn(GC_BUILDER.withId(id).build())
                    .when(mockRepository).save(certificate);

            GiftCertificateResponse actual = certificateService.update(id, request);

            verify(mockRepository).save(certificate);
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void checkUpdateShouldThrowEntityNotFoundException() {
            Integer id = -1;

            doReturn(Optional.empty())
                    .when(mockRepository).findById(id);

            assertThrows(EntityNotFoundException.class,
                    () -> certificateService.update(id, any()));
        }
    }

    @Nested
    class FindAllTest {

        private static final int page = 0;
        private static final int size = 10;

        @Test
        void checkFindAllShouldReturnSameList() {
            Pageable pageable = PageRequest.of(page, size);
            Page<GiftCertificate> certificates = new PageImpl<>(List.of(
                    GC_BUILDER.withName("some name").build(),
                    GC_BUILDER.withName("other name").build()
            ), pageable, 20);

            doReturn(certificates)
                    .when(mockRepository).findAll(pageable);

            Page<GiftCertificateResponse> actual = certificateService.findAll(PageRequest.of(page, size));

            verify(mockRepository).findAll(pageable);
            assertThat(actual.getContent())
                    .containsAll(certificates.getContent()
                            .stream()
                            .map(certificateMapper::toResponse)
                            .toList());
        }

        @Test
        void checkFindAllWithParamsShouldReturnSameList() {
            GiftCertificate certificate = GC_BUILDER.build();
            GiftCertificateResponse expectedResponse = certificateMapper.toResponse(certificate);
            Page<GiftCertificateResponse> expectedPage = new PageImpl<>(List.of(expectedResponse));

            doReturn(new PageImpl<>(List.of(certificate)))
                    .when(mockRepository).findAll(any(), any(), any());

            Page<GiftCertificateResponse> actualPage = certificateService
                    .findAll(any(), any(), any());

            verify(mockRepository).findAll(any(), any(), any());
            assertThat(actualPage).isEqualTo(expectedPage);
        }
    }


    @Nested
    class FindByIdTest {

        @Test
        void checkFindShouldCallRepositoryWithSameId() {
            Integer id = 1;
            GiftCertificate certificate = GC_BUILDER
                    .withId(id)
                    .build();
            GiftCertificateResponse expectedResponse = certificateMapper.toResponse(certificate);
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            doReturn(Optional.of(certificate)).
                    when(mockRepository).findById(id);

            GiftCertificateResponse actualResult = certificateService.find(id);

            verify(mockRepository).findById(idCaptor.capture());
            assertThat(actualResult).isEqualTo(expectedResponse);
            Integer actualId = idCaptor.getValue();
            assertThat(actualId).isEqualTo(id);
        }

        @Test
        void checkFindShouldThrowEntityNotFoundException() {
            Integer id = 1;

            doReturn(Optional.empty())
                    .when(mockRepository).findById(id);

            assertThrows(EntityNotFoundException.class,
                    () -> certificateService.find(id));
        }

    }
}
