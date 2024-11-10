package pl.reactive11.product_calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.reactive11.product_calculator.discount.DiscountPolicyEntity;
import pl.reactive11.product_calculator.discount.DiscountPolicyRepository;
import pl.reactive11.product_calculator.discount.DiscountPolicyType;
import pl.reactive11.product_calculator.discount.DiscountPolicyValuesEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.RoundingMode.HALF_EVEN;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    @Mock
    DiscountPolicyRepository repository;

    @InjectMocks
    DiscountService discountService;


    @Test
    void shouldCalculatePercentageDiscount(){
        List<ProductPriceAndQuantity> productPriceAndQuantity = List.of(
                new ProductPriceAndQuantity(UUID.randomUUID(), 200L, BigDecimal.valueOf(500.0)),
                new ProductPriceAndQuantity(UUID.randomUUID(), 500L, BigDecimal.valueOf(450.0))
        );
        Mockito.when(repository.findActiveAmountBasedDiscountPolicy()).thenReturn(Optional.empty());
        Mockito.when(repository.findActivePercentageBasedDiscountPolicy()).thenReturn(buildPercentageDiscountPolicy());

        BigDecimal expectedDiscountPerItem = BigDecimal.valueOf(0.05).multiply(BigDecimal.valueOf(950.0)).divide(BigDecimal.valueOf(700.0), 5 ,HALF_EVEN);

        List<ProductPriceAndQuantity> expected = List.of(
                new ProductPriceAndQuantity(productPriceAndQuantity.get(0).getProductId(), productPriceAndQuantity.get(0).getQuantity(), productPriceAndQuantity.get(0).getPrice().subtract(expectedDiscountPerItem.multiply(BigDecimal.valueOf(200)))),
                new ProductPriceAndQuantity(productPriceAndQuantity.get(1).getProductId(), productPriceAndQuantity.get(1).getQuantity(), productPriceAndQuantity.get(1).getPrice().subtract(expectedDiscountPerItem.multiply(BigDecimal.valueOf(500))))
        );
        List<ProductPriceAndQuantity> actual = discountService.applyDiscountPolicy(productPriceAndQuantity);
        Assertions.assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void shouldCalculateAmountDiscount(){
        List<ProductPriceAndQuantity> productPriceAndQuantity = List.of(
                new ProductPriceAndQuantity(UUID.randomUUID(), 200L, BigDecimal.valueOf(500.0)),
                new ProductPriceAndQuantity(UUID.randomUUID(), 500L, BigDecimal.valueOf(450.0))
        );
        Mockito.when(repository.findActiveAmountBasedDiscountPolicy()).thenReturn(buildAmountDiscountPolicy());
        Mockito.when(repository.findActivePercentageBasedDiscountPolicy()).thenReturn(Optional.empty());
        List<ProductPriceAndQuantity> expected = List.of(
                new ProductPriceAndQuantity(productPriceAndQuantity.get(0).getProductId(), productPriceAndQuantity.get(0).getQuantity(), productPriceAndQuantity.get(0).getPrice().subtract(BigDecimal.valueOf(4)).setScale(2, RoundingMode.HALF_EVEN)),
                new ProductPriceAndQuantity(productPriceAndQuantity.get(1).getProductId(), productPriceAndQuantity.get(1).getQuantity(), productPriceAndQuantity.get(1).getPrice().subtract(BigDecimal.valueOf(5)).setScale(2, RoundingMode.HALF_EVEN))
        );
        List<ProductPriceAndQuantity> actual = discountService.applyDiscountPolicy(productPriceAndQuantity);
        Assertions.assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void shouldCalculateDiscountWhereAllDiscountPoliciesAreDisabled(){
        List<ProductPriceAndQuantity> productPriceAndQuantity = List.of(
                new ProductPriceAndQuantity(UUID.randomUUID(), 200L, BigDecimal.valueOf(500.0)),
                new ProductPriceAndQuantity(UUID.randomUUID(), 500L, BigDecimal.valueOf(450.0))
        );
        Mockito.when(repository.findActiveAmountBasedDiscountPolicy()).thenReturn(Optional.empty());
        Mockito.when(repository.findActivePercentageBasedDiscountPolicy()).thenReturn(Optional.empty());
        List<ProductPriceAndQuantity> actual = discountService.applyDiscountPolicy(productPriceAndQuantity);
        Assertions.assertThat(actual).hasSameElementsAs(productPriceAndQuantity);
    }

    private Optional<DiscountPolicyEntity> buildAmountDiscountPolicy() {
        List<DiscountPolicyValuesEntity> discountPolicyValues = new ArrayList<>();
        discountPolicyValues.add(new DiscountPolicyValuesEntity(0L, 50.0, 1.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(1L, 100.0, 2.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(2L, 150.0, 3.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(3L, 200.0, 4.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(4L, 300.0, 5.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(5L, 600.0, 6.0));
        return Optional.of(new DiscountPolicyEntity(0L, true, DiscountPolicyType.AMOUNT, discountPolicyValues));
    }

    private Optional<DiscountPolicyEntity> buildPercentageDiscountPolicy() {
        List<DiscountPolicyValuesEntity> discountPolicyValues = new ArrayList<>();
        discountPolicyValues.add(new DiscountPolicyValuesEntity(0L, 50.0, 1.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(1L, 300.0, 4.0));
        discountPolicyValues.add(new DiscountPolicyValuesEntity(2L, 500.0, 5.0));
        return Optional.of(new DiscountPolicyEntity(0L, true, DiscountPolicyType.PERCENTAGE, discountPolicyValues));
    }
}