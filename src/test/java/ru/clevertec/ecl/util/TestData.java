package ru.clevertec.ecl.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.User;

import java.util.List;

public class TestData {

    public static GiftCertificate defaultCertificate() {
        return GiftCertificate.builder()
                .duration(10)
                .name("some name")
                .description("some description")
                .build();
    }

    public static Pageable defaultPageable() {
        return PageRequest.of(0, 10);
    }

    public static Page<User> defaultPageWithUsers() {
        UserTestBuilder userBuilder = new UserTestBuilder();
        return new PageImpl<>(List.of(
                userBuilder.build(),
                userBuilder.withId(12).build(),
                userBuilder.withName("bla-bla").build()
        ));
    }

}
