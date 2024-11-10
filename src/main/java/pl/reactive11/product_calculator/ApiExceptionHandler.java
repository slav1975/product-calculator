package pl.reactive11.product_calculator;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import pl.reactive11.product_calculator.discount.DiscountPolicyNotFoundException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ApiExceptionHandler {
    @ExceptionHandler({
            ProductIdNotFoundException.class,
            HttpMessageNotReadableException.class,
            HandlerMethodValidationException.class,
            DiscountPolicyNotFoundException.class})
    public ResponseEntity<String> badRequest(Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

}
