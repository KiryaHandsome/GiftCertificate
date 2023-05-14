package ru.clevertec.ecl.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class OrderResponse {

    @NotNull
    private Double totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY hh:mm:ss", timezone = "UTC")
    private ZonedDateTime purchaseDate;
}
