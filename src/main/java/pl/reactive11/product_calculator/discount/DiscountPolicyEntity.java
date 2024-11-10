package pl.reactive11.product_calculator.discount;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PRIVATE;

@Entity
@FieldDefaults(level = PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    boolean enabled;
    @Enumerated(STRING)
    DiscountPolicyType policyType;
    @OneToMany(cascade = CascadeType.ALL, fetch = EAGER, mappedBy = "discountPolicy")
    List<DiscountPolicyValuesEntity> discountPolicyValues;
    void addDiscountPolicyValuesEntities(List<DiscountPolicyValuesEntity> entities){
        this.discountPolicyValues = entities;
        entities.forEach(discountPolicyValuesEntity -> discountPolicyValuesEntity.setDiscountPolicy(this));
    }
}
