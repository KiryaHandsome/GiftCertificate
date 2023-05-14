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
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.mapper.TagMapperImpl;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.TagTestBuilder;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    private static final String TAG_PATH = "/tags";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;
    private TagTestBuilder TEST_BUILDER;
    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        tagMapper = (TagMapper) new TagMapperImpl();
        TEST_BUILDER = new TagTestBuilder();
    }


    @Nested
    class GetAllEndpointTest {

        @Test
        void checkGetAllShouldReturnCorrectResponse() throws Exception {
            var expected = new PageImpl<>(List.of(TEST_BUILDER.build()))
                    .map(tagMapper::toResponse);

            doReturn(expected)
                    .when(tagService).findAll(any());

            mockMvc.perform(get(URI.create(TAG_PATH)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)))
                    .andDo(print());
        }
    }

    @Nested
    class GetByIdEndpointTest {

        @Test
        void checkGetByIdShouldReturnBadRequestStatus() throws Exception {
            int id = 10;

            doThrow(EntityNotFoundException.class)
                    .when(tagService).find(id);

            mockMvc.perform(get(URI.create(TAG_PATH + "/" + id)))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        void checkGetByIdShouldReturnCorrectResponse() throws Exception {
            int id = 10;
            var expected = tagMapper.toResponse(TEST_BUILDER.build());

            doReturn(expected)
                    .when(tagService).find(id);

            mockMvc.perform(get(URI.create(TAG_PATH + "/" + id)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));
        }
    }

    @Nested
    class UpdateEndpointTest {

        @Test
        void checkUpdateShouldReturnExpectedResponse() throws Exception {
            int id = 10;
            String newName = "Name";
            var request = new TagRequest(newName);
            var response = tagMapper.toResponse(TEST_BUILDER.withName(newName).withId(id).build());

            doReturn(response)
                    .when(tagService).update(id, request);

            mockMvc.perform(put(URI.create(TAG_PATH + "/" + id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(response)));

            verify(tagService).update(id, request);
        }

        @Test
        void checkUpdateShouldReturnNotFoundStatus() throws Exception {
            int id = 1;
            var request = new TagRequest("name");

            doThrow(EntityNotFoundException.class)
                    .when(tagService).update(id, request);

            mockMvc.perform(put(URI.create(TAG_PATH + "/" + id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            verify(tagService).update(id, request);
        }
    }

    @Nested
    class DeleteEndpointTest {

        @Test
        void checkDeleteShouldReturnNoContentStatus() throws Exception {
            int id = 1;

            doNothing()
                    .when(tagService).delete(id);

            mockMvc.perform(delete(URI.create(TAG_PATH + "/" + id)))
                    .andExpect(status().isNoContent());

            verify(tagService).delete(id);
        }
    }

    @Nested
    class CreateEndpointTest {

        @Test
        void checkCreateShouldReturnLocation() throws Exception {
            int id = 1;
            var request = tagMapper.toRequest(TEST_BUILDER.build());
            var response = tagMapper.toResponse(TEST_BUILDER.withId(id).build());

            doReturn(response)
                    .when(tagService).save(request);
            mockMvc.perform(post(URI.create(TAG_PATH))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", TAG_PATH + "/" + id));

            verify(tagService).save(request);
        }
    }
}
