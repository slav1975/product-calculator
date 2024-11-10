package pl.reactive11.product_calculator.discount;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@Builder
class CreateDiscountPolicyRequest {

    boolean enabled;
    @NotNull
    DiscountPolicyType policyType;
    @NotNull
    List<DiscountValue> discountValueList;

    @Data
    @AllArgsConstructor
    static class DiscountValue {
        Double from;
        Double value;
    }
}
