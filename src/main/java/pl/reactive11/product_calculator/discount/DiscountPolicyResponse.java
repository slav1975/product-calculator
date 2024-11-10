package pl.reactive11.product_calculator.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Builder
class DiscountPolicyResponse {

    Long id;
    boolean enabled;
    DiscountPolicyType policyType;
    List<DiscountValue> discountValues;

    @Data
    @AllArgsConstructor
    static class DiscountValue{
        Long id;
        Double from;
        Double value;
    }
}
