package pl.reactive11.product_calculator.discount;

public class DiscountPolicyNotFoundException extends RuntimeException {
    public DiscountPolicyNotFoundException(String message) {
        super(message);
    }
}
