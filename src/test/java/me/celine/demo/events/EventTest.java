package me.celine.demo.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test // 빌더가 있는지 테스트
    public void builder() {
        Event event = Event.builder()
                .name("kite")
                .description("설명글")
                .build();
        assertThat(event).isNotNull();
    }

    @Test  // JavaBean의 기본 생성자 제공, Getter/Setter 메서드 지원 확인하는 테스트
    public void javabean() {

        //Given
        String name = "celine";
        String description = "celine's comment";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}
