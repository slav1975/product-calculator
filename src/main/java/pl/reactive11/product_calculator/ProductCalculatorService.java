package pl.reactive11.product_calculator;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.reactive11.product_calculator.product.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
@Slf4j
class ProductCalculatorService {

    ProductRepository productRepository;

    DiscountService discountService;

    private static ProductPriceAndQuantity priceOfQuantity(ProductPriceAndQuantity productPriceAndQuantity) {
        return new ProductPriceAndQuantity(
                productPriceAndQuantity.getProductId(),
                productPriceAndQuantity.getQuantity(),
                productPriceAndQuantity.getPrice().multiply(valueOf(productPriceAndQuantity.getQuantity())));
    }

    CalculatePriceResponse calculatePrice(CalculatePriceRequest productItemList) {
        List<ProductPriceAndQuantity> initialState = initialState(productItemList);
        var priceWithoutDiscount = initialState.stream().map(ProductCalculatorService::priceOfQuantity).collect(toList());
        return buildResponse(discountService.applyDiscountPolicy(priceWithoutDiscount));
    }

    private CalculatePriceResponse buildResponse(List<ProductPriceAndQuantity> productPriceAndQuantity) {
        log.debug("Price with all discounts: \n" + productPriceAndQuantity + "\n");
        return new CalculatePriceResponse(productPriceAndQuantity
                .stream()
                .collect(Collectors.toMap(
                        item ->  item.getProductId().toString(),
                        item -> item.getPrice().setScale(2, HALF_EVEN).doubleValue())));
    }

    private List<ProductPriceAndQuantity> initialState(CalculatePriceRequest productItemList) {
        var productIds = productItemList.keySet().stream().map(UUID::fromString).collect(toList());
        var entities = productRepository.findAllById(productIds);
        if (productItemList.size() > entities.size()) {
            var notExistIds = productItemList.keySet();
            notExistIds.removeAll(entities.stream().map(productEntity -> productEntity.getId().toString()).toList());
            throw new ProductIdNotFoundException("Following product id(s) not exist: " + String.join(", ", notExistIds));
        }
        return entities
                .stream()
                .map(productEntity -> new ProductPriceAndQuantity(
                        productEntity.getId(),
                        productItemList.get(productEntity.getId().toString()),
                        productEntity.getPrice()))
                .collect(toList());
    }


}
