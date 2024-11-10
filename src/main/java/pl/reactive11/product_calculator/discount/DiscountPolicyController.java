package pl.reactive11.product_calculator.discount;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
class DiscountPolicyController {

    DiscountPolicyService discountPolicyService;

    @Operation(summary = "Enables/Disables discount policy")
    @PostMapping(path = "/discounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    void activateDiscountPolicy(@PathVariable Long id, @RequestParam boolean enabled) {
        discountPolicyService.activateDiscountPolicy(id, enabled);
    }

    @Operation(summary = "Create discount policy.")
    @PostMapping(path = "/discounts", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    void create(@Valid @RequestBody CreateDiscountPolicyRequest request) {
        discountPolicyService.create(request);
    }

    @Operation(summary = "Gets all discount policies")
    @GetMapping(path = "/discounts")
    ResponseEntity<List<DiscountPolicyResponse>> findAll() {
        return ResponseEntity.ok(discountPolicyService.findAll());
    }

}
