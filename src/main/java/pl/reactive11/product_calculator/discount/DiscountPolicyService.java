package pl.reactive11.product_calculator.discount;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
public class DiscountPolicyService {

    DiscountPolicyRepository repository;

    @Transactional
    void create(CreateDiscountPolicyRequest request) {
        if (request.isEnabled()) {
            repository.disableActivePolicy(request.getPolicyType());
        }
        DiscountPolicyEntity discountPolicy = new DiscountPolicyEntity();
        discountPolicy.setEnabled(request.isEnabled());
        discountPolicy.setPolicyType(request.getPolicyType());
        discountPolicy.addDiscountPolicyValuesEntities(request.getDiscountValueList().stream()
                .map(this::buildDiscountPolicyValuesEntity).collect(toList()));
        repository.save(discountPolicy);
    }

    @Transactional
    void activateDiscountPolicy(Long id, boolean enabled) {

        var discountPolicyEntity = discountPolicyEntity(id);

        if (enabled) {
            repository.disableActivePolicy(discountPolicyEntity.getPolicyType());
            repository.enableActivePolicy(id);
            return;
        }

        if(discountPolicyEntity.isEnabled()){
            repository.disableActivePolicy(discountPolicyEntity.getPolicyType());
        }

    }

    private DiscountPolicyEntity discountPolicyEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DiscountPolicyNotFoundException("Discount policy with id: '" + id + "' doesn't exist"));
    }

    List<DiscountPolicyResponse> findAll() {
        return repository.findAll().stream().map(DiscountPolicyService::apply).collect(toList());
    }

    private DiscountPolicyValuesEntity buildDiscountPolicyValuesEntity(CreateDiscountPolicyRequest.DiscountValue discountValue) {
        DiscountPolicyValuesEntity entity = new DiscountPolicyValuesEntity();
        entity.setCalculateFrom(discountValue.getFrom());
        entity.setCalculateValue(discountValue.getValue());
        return entity;
    }

    private static DiscountPolicyResponse.DiscountValue apply(DiscountPolicyValuesEntity discountPolicyValuesEntity) {
        return new DiscountPolicyResponse.DiscountValue(discountPolicyValuesEntity.getId(), discountPolicyValuesEntity.getCalculateFrom(), discountPolicyValuesEntity.getCalculateValue());
    }

    private static DiscountPolicyResponse apply(DiscountPolicyEntity discountPolicyEntity) {
        return DiscountPolicyResponse
                .builder()
                .id(discountPolicyEntity.getId())
                .enabled(discountPolicyEntity.isEnabled())
                .discountValues(discountPolicyEntity.getDiscountPolicyValues()
                        .stream().map(DiscountPolicyService::apply).collect(toList()))
                .policyType(discountPolicyEntity.getPolicyType())
                .build();
    }

}
