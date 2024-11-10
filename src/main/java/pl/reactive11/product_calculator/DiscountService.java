package pl.reactive11.product_calculator;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.reactive11.product_calculator.discount.DiscountPolicyEntity;
import pl.reactive11.product_calculator.discount.DiscountPolicyRepository;
import pl.reactive11.product_calculator.discount.DiscountPolicyValuesEntity;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
@Slf4j
class DiscountService {

    DiscountPolicyRepository discountPolicyRepository;

    public List<ProductPriceAndQuantity> applyDiscountPolicy(List<ProductPriceAndQuantity> productPriceAndQuantity) {

        log.debug("Price without discount : \n" + productPriceAndQuantity + "\n");

        Map<UUID, BigDecimal> discountAccumulator = new HashMap<>();

        discountPolicyRepository.findActiveAmountBasedDiscountPolicy().ifPresent(discountPolicy -> {
            productPriceAndQuantity.forEach(item -> {
                var amountBasedDiscount = amountBasedDiscount(discountPolicy, item.getQuantity());
                discountAccumulator.put(item.getProductId(), amountBasedDiscount);
            });
        });

        log.debug("Account discount values: \n" + discountAccumulator.entrySet().stream().map(uuidBigDecimalEntry -> uuidBigDecimalEntry.getKey() + " : " + uuidBigDecimalEntry.getValue().doubleValue()).collect(joining(",\n")) + "\n");

        discountPolicyRepository.findActivePercentageBasedDiscountPolicy().ifPresent(discountPolicy -> {
            Long allItemsCount = productPriceAndQuantity.stream().map(ProductPriceAndQuantity::getQuantity).reduce(0L, Long::sum);
            BigDecimal totalPrice = productPriceAndQuantity.stream().map(ProductPriceAndQuantity::getPrice).reduce(ZERO, BigDecimal::add);
            BigDecimal discountForAllItems = fractionDiscount(discountPolicy, allItemsCount).multiply(totalPrice);
            BigDecimal discountPerItem = discountForAllItems.divide(BigDecimal.valueOf(allItemsCount), HALF_EVEN);
            productPriceAndQuantity.forEach(item -> {
                var discountPerProduct = discountPerItem.multiply(BigDecimal.valueOf(item.getQuantity()));
                ofNullable(discountAccumulator.putIfAbsent(item.getProductId(), discountPerProduct))
                        .ifPresent(value -> discountAccumulator.put(item.getProductId(), value.add(discountPerProduct)));
            });
        });

        log.debug("Account + Percentage discount values: \n" + discountAccumulator.entrySet().stream().map(uuidBigDecimalEntry -> uuidBigDecimalEntry.getKey() + " : " + uuidBigDecimalEntry.getValue().doubleValue()).collect(joining(",\n")) + "\n");

        return productPriceAndQuantity.stream().map(item ->
                new ProductPriceAndQuantity(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice().subtract(Optional.ofNullable(discountAccumulator.get(item.getProductId())).orElse(ZERO))))
                .collect(toList());

    }

    private BigDecimal fractionDiscount(DiscountPolicyEntity discountPolicy, Long itemCount) {
        return findDiscountValue(discountPolicy.getDiscountPolicyValues(), itemCount).multiply(BigDecimal.valueOf(0.01));
    }

    private BigDecimal amountBasedDiscount(DiscountPolicyEntity discountPolicy, Long quantity) {
        return findDiscountValue(discountPolicy.getDiscountPolicyValues(), quantity);
    }

    private BigDecimal findDiscountValue(List<DiscountPolicyValuesEntity> discountPolicyValues, Long quantity) {
        return BigDecimal.valueOf(discountPolicyValues
                .stream()
                .sorted(Comparator.comparingDouble(DiscountPolicyValuesEntity::getCalculateFrom).reversed())
                .filter(discountPolicyValuesEntity -> discountPolicyValuesEntity.getCalculateFrom() <= quantity)
                .findFirst()
                .map(DiscountPolicyValuesEntity::getCalculateValue)
                .orElse(0.0)).setScale(2, HALF_EVEN);
    }
}
