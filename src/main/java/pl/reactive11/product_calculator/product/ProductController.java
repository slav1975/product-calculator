package pl.reactive11.product_calculator.product;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
class ProductController {

    ProductRepository productRepository;

    @Operation(summary = "Creates products")
    @PostMapping(path = "/products", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(@RequestBody List<@Valid CreateProductRequest> request) {
        productRepository.saveAll(request.stream().map(ProductController::apply).collect(toList()));
        return ResponseEntity.status(CREATED).build();
    }
    @Operation(summary = "Finds all products")
    @GetMapping(path = "/products")
    ResponseEntity<List<ProductResponse>> find() {
        return ResponseEntity.ok(productRepository.findAll().stream().map(ProductController::apply).collect(toList()));
    }

    private static ProductEntity apply(CreateProductRequest createProductRequest) {
        return ProductEntity.builder().name(createProductRequest.getName()).
                price(BigDecimal.valueOf(createProductRequest.getPrice())
                        .setScale(2, RoundingMode.HALF_EVEN)).build();
    }

    private static ProductResponse apply(ProductEntity productEntity) {
        return ProductResponse.builder().name(productEntity.getName())
                .id(productEntity.getId().toString()).price(productEntity.getPrice()
                        .setScale(2, RoundingMode.HALF_EVEN).toString()).build();
    }

}
