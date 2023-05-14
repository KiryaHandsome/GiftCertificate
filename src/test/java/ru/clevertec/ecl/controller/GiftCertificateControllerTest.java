package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.dto.certificate.GiftCertificateRequest;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.GiftCertificateMapperImpl;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.util.GiftCertificateTestBuilder;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GiftCertificateController.class)
public class GiftCertificateControllerTest {

    public static final String CERTIFICATE_PATH = "/gift-certificates";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftCertificateService certificateService;

    @Autowired
    private ObjectMapper objectMapper;
    private GiftCertificateMapper certificateMapper;
    private GiftCertificateTestBuilder TEST_BUILDER;


    @BeforeEach
    void setUp() {
        TEST_BUILDER = new GiftCertificateTestBuilder();
        certificateMapper = (GiftCertificateMapper) new GiftCertificateMapperImpl();
    }

    @Nested
    class GetAllEndpointTest {

        @Test
        void checkGetAllShouldReturnCorrectResponse() throws Exception {
            var expectedContent = new PageImpl<>(List.of(TEST_BUILDER.build()))
                    .map(certificateMapper::toResponse);

            doReturn(expectedContent)
                    .when(certificateService).findAll(any(), any(), any());

            mockMvc.perform(get(URI.create(CERTIFICATE_PATH)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expectedContent)))
                    .andDo(print());
        }
    }

    @Nested
    class GetByIdEndpointTest {

        @Test
        void checkGetByIdShouldReturnBadRequestStatus() throws Exception {
            int id = 10;

            doThrow(EntityNotFoundException.class)
                    .when(certificateService).find(id);

            mockMvc.perform(get(URI.create(CERTIFICATE_PATH + "/" + id)))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        void checkGetByIdShouldReturnCorrectResponse() throws Exception {
            int id = 10;
            var expectedResponse = certificateMapper.toResponse(TEST_BUILDER.build());

            doReturn(expectedResponse)
                    .when(certificateService).find(id);

            mockMvc.perform(get(URI.create(CERTIFICATE_PATH + "/" + id)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
        }
    }

    @Nested
    class UpdateEndpointTest {

        @Test
        void checkUpdateShouldReturnExpectedResponse() throws Exception {
            var response = certificateMapper.toResponse(TEST_BUILDER.build());
            var request = new GiftCertificateRequest();
            int id = 10;

            doReturn(response)
                    .when(certificateService).update(id, request);

            mockMvc.perform(patch(URI.create(CERTIFICATE_PATH + "/" + id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(response)));

            verify(certificateService).update(id, request);
        }

        @Test
        void checkUpdateShouldReturnBadRequestStatus() throws Exception {
            int id = 1;
            var request = new GiftCertificateRequest();

            doThrow(EntityNotFoundException.class)
                    .when(certificateService).update(id, request);

            mockMvc.perform(patch(URI.create(CERTIFICATE_PATH + "/" + id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            verify(certificateService).update(id, request);
        }
    }

    @Nested
    class DeleteEndpointTest {

        @Test
        void checkDeleteShouldReturnNoContentStatus() throws Exception {
            int id = 1;

            doNothing()
                    .when(certificateService).delete(id);

            mockMvc.perform(delete(URI.create(CERTIFICATE_PATH + "/" + id)))
                    .andExpect(status().isNoContent());

            verify(certificateService).delete(id);
        }
    }

    @Nested
    class CreateEndpointTest {

        @Test
        void checkCreateShouldReturnCreatedEntity() throws Exception {
            var request = certificateMapper.toRequest(TEST_BUILDER.build());
            var response = certificateMapper.toResponse(TEST_BUILDER.build());

            doReturn(response)
                    .when(certificateService).save(request);

            mockMvc.perform(post(URI.create(CERTIFICATE_PATH))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", CERTIFICATE_PATH + "/" + response.getId()));

            verify(certificateService).save(request);
        }
    }

}
