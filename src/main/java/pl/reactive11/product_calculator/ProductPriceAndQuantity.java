package pl.reactive11.product_calculator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class ProductPriceAndQuantity {
    UUID productId;
    Long quantity;
    BigDecimal price;
}
