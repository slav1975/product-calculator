package pl.reactive11.product_calculator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class CalculatePriceResponse extends LinkedHashMap<String, Double> {
    public CalculatePriceResponse(Map<? extends String, ? extends Double> m) {super(m);}
}
