package pl.reactive11.product_calculator.discount;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface DiscountPolicyRepository extends ListCrudRepository<DiscountPolicyEntity, Long> {

    @Query(value = "SELECT e FROM DiscountPolicyEntity e WHERE e.enabled = true AND policyType = 'AMOUNT'")
    Optional<DiscountPolicyEntity> findActiveAmountBasedDiscountPolicy();

    @Query(value = "SELECT e FROM DiscountPolicyEntity e WHERE e.enabled = true AND policyType = 'PERCENTAGE'")
    Optional<DiscountPolicyEntity> findActivePercentageBasedDiscountPolicy();

    @Modifying
    @Query(value = "UPDATE DiscountPolicyEntity e set e.enabled = false WHERE e.policyType = :policyType and e.enabled = true")
    void disableActivePolicy(DiscountPolicyType policyType);

    @Modifying
    @Query(value = "UPDATE DiscountPolicyEntity e set e.enabled = true WHERE e.id = :id")
    void enableActivePolicy(Long id);
}
