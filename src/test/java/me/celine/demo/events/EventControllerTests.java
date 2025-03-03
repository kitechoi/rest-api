package me.celine.demo.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("spring comment")
                .beginEnrollmentDateTime(LocalDateTime.of(2025,3,1, 14,18))
                .closeEnrollmentDateTime(LocalDateTime.of(2025,3,3,14,18))
                .beginEventDateTime(LocalDateTime.of(2025,3,4,15,0))
                .endEventDateTime(LocalDateTime.of(2025,3,6,15,5))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();
//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print()) // 어떤 요청을 받고, 응답을 줬는지 확인
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()) // 응답 Body 안에 id가 있는지 확인
                .andExpect(header().exists(HttpHeaders.LOCATION))   // HTTP 응답 헤더에 LOCATION 필드가 있는지 확인
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE ))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }


    @Test
    public void create_Bad_Event() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("spring comment")
                .beginEnrollmentDateTime(LocalDateTime.of(2025,3,1, 14,18))
                .closeEnrollmentDateTime(LocalDateTime.of(2025,3,3,14,18))
                .beginEventDateTime(LocalDateTime.of(2025,3,4,15,0))
                .endEventDateTime(LocalDateTime.of(2025,3,6,15,5))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print()) // 어떤 요청을 받고, 응답을 줬는지 확인
                .andExpect(status().isBadRequest())
        ;
    }

    // EventDto 에 아무것도 들어있지 않을 때 400 반환하는지 테스트
    @Test
    public void createEvent_Bad_Request_empty() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }
}
