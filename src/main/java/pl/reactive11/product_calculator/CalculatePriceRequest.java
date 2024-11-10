package pl.reactive11.product_calculator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


class CalculatePriceRequest extends LinkedHashMap<String, Long> {

    public CalculatePriceRequest() {}

    public CalculatePriceRequest(Map<? extends String, ? extends Long> m) {
        super(m);
    }

    Long allCountOfItems() {
        return (Long) this.values().stream().mapToLong(Long::longValue).sum();
    }

}
