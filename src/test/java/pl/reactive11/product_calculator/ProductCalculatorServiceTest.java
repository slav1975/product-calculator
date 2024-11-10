package pl.reactive11.product_calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.reactive11.product_calculator.product.ProductEntity;
import pl.reactive11.product_calculator.product.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductCalculatorServiceTest {

    @Mock
    DiscountService discountService;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductCalculatorService productCalculatorService;


    @Test
    void shouldCalculate() {
        var calculatePriceRequest = new CalculatePriceRequest();
        calculatePriceRequest.put("c6d6cce5-6273-4133-870a-8a70b6f62001", 10L);
        calculatePriceRequest.put("c6d6cce5-6273-4133-870a-8a70b6f62002", 15L);
        calculatePriceRequest.put("c6d6cce5-6273-4133-870a-8a70b6f62003", 30L);

        Mockito.when(productRepository.findAllById(any())).thenReturn(
                List.of(
                        new ProductEntity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62001"), "Samsung Galaxy", new BigDecimal("100.99")),
                        new ProductEntity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62002"), "Motorola G22", new BigDecimal("90.99")),
                        new ProductEntity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62003"), "Nokia Lumia", new BigDecimal("80.99"))
                        ));

        Mockito.when(discountService.applyDiscountPolicy(any())).thenReturn(List.of(
                new ProductPriceAndQuantity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62001"),10L, BigDecimal.valueOf(1009.90)),
                new ProductPriceAndQuantity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62002"),15L, BigDecimal.valueOf(1364.85)),
                new ProductPriceAndQuantity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62003"),30L, BigDecimal.valueOf(2429.70))
        ));

        var expected = new CalculatePriceResponse();
        expected.put("c6d6cce5-6273-4133-870a-8a70b6f62001", 1009.90);
        expected.put("c6d6cce5-6273-4133-870a-8a70b6f62002", 1364.85);
        expected.put("c6d6cce5-6273-4133-870a-8a70b6f62003", 2429.70);


        var actual = productCalculatorService.calculatePrice(calculatePriceRequest);

        Assertions.assertThat(actual).containsAllEntriesOf(expected);
    }

    @Test
    void shouldThrowExceptionWhenRequestedProductsDoesntExistInRepository() {
        var calculatePriceRequest = new CalculatePriceRequest();
        calculatePriceRequest.put("c6d6cce5-6273-4133-870a-8a70b6f62001", 10L);
        calculatePriceRequest.put("c6d6cce5-6273-4133-870a-8a70b6f62002", 15L);
        calculatePriceRequest.put("c6d6cce5-6273-4133-870a-8a70b6f62003", 30L);

        var entity = new ProductEntity(UUID.fromString("c6d6cce5-6273-4133-870a-8a70b6f62002"), "Samsung Galaxy", new BigDecimal("100.0"));
        Mockito.when(productRepository.findAllById(any())).thenReturn(List.of(entity));

        Assertions.assertThatThrownBy(() -> {
            productCalculatorService.calculatePrice(calculatePriceRequest);
        }).isInstanceOf(ProductIdNotFoundException.class)
                .hasMessage("Following product id(s) not exist: c6d6cce5-6273-4133-870a-8a70b6f62001, c6d6cce5-6273-4133-870a-8a70b6f62003");

    }

}
