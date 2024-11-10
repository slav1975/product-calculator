package pl.reactive11.product_calculator.product;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Builder
class ProductResponse {
    String id;
    String name;
    String price;
}
