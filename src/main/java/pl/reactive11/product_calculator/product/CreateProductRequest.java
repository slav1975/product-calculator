package pl.reactive11.product_calculator.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
class CreateProductRequest {
    @NotEmpty(message = "A name must be not empty")
    String name;
    @Positive(message = "A price must be positive")
    @NotNull(message = "A price must be not null")
    Double price;
}
