package pl.reactive11.product_calculator;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
class ProductCalculatorController {

    ProductCalculatorService productCalculatorService;

    @PostMapping(path = "/calculate", consumes = APPLICATION_JSON_VALUE)
     ResponseEntity<CalculatePriceResponse> calculatePrice(@RequestBody CalculatePriceRequest request) {
        return ResponseEntity.ok(productCalculatorService.calculatePrice(request));
    }
}
