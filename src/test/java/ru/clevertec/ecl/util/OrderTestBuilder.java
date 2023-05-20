package ru.clevertec.ecl.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;

import java.time.ZonedDateTime;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTestBuilder {

    private Integer id = 1;
    private User user = new UserTestBuilder().build();
    private GiftCertificate certificate = new GiftCertificateTestBuilder().build();
    private ZonedDateTime purchaseDate = null;
    private Double totalCost = 213.12;

    public Order build() {
        return Order.builder()
                .id(id)
                .user(user)
                .totalCost(totalCost)
                .certificate(certificate)
                .purchaseDate(purchaseDate)
                .build();
    }
}
