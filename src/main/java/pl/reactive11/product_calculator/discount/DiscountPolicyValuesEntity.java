package pl.reactive11.product_calculator.discount;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@FieldDefaults(level = PRIVATE)
@Data
@NoArgsConstructor
public class DiscountPolicyValuesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double calculateFrom;
    Double calculateValue;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "discount_policy_id")
    DiscountPolicyEntity discountPolicy;

    public DiscountPolicyValuesEntity(Long id, Double calculateFrom, Double calculateValue) {
        this.id = id;
        this.calculateFrom = calculateFrom;
        this.calculateValue = calculateValue;
    }
}
