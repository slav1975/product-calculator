package pl.reactive11.product_calculator;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.reactive11.product_calculator.TestUtils.getContent;

@WebMvcTest(controllers = ProductCalculatorController.class)
class ProductCalculatorControllerTest extends TestContext {

    @MockBean
    ProductCalculatorService productCalculatorService;

    @InjectMocks
    ProductCalculatorController productCalculatorController;

    //@Test
    public void shouldThrowExceptionWhenProductsNotFound() throws Exception {

        CalculatePriceRequest request = new CalculatePriceRequest();
        request.put("c6d6cce5-6273-4133-870a-8a70b6f62539", 10L);
        request.put("74c77c10-b6aa-4e04-b520-9b2649e21e6b", 11L);

        //TODO argThat(request::equals) doesn't work correctly
        Mockito.when(productCalculatorService.calculatePrice(argThat(request::equals)))
                .thenThrow(new ProductIdNotFoundException("Following product id(s) not exist: c6d6cce5-6273-4133-870a-8a70b6f62539, 74c77c10-b6aa-4e04-b520-9b2649e21e6b"));

        mockMvc.perform(post("/calculate")
                        .contentType(APPLICATION_JSON)
                        .content(getContent("CalculatePriceRequest.json")))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    //@Test
    public void shouldThrowExceptionWhenAnyQuantityIsNotPositiveLong() throws Exception {
        mockMvc.perform(post("/calculate")
                        .contentType(APPLICATION_JSON)
                        .content(getContent("CalculatePriceRequest.QuantityIsNotPositive.json")))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    //@Test
    public void shouldThrowExceptionWhenAnyProductIdIsEmpty() throws Exception {
        mockMvc.perform(post("/calculate")
                        .contentType(APPLICATION_JSON)
                        .content(getContent("CalculatePriceRequest.ProductIdIsEmpty.json")))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    //@Test
    public void shouldThrowExceptionWhenAnyProductIdHasNotUUIDFormat() throws Exception {
        mockMvc.perform(post("/calculate")
                        .contentType(APPLICATION_JSON)
                        .content(getContent("CalculatePriceRequest.NotUUIDFormat.json")))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    //@Test
    public void shouldCalculatePriceOfItems() throws Exception {

        CalculatePriceRequest request = new CalculatePriceRequest();
        request.put("c6d6cce5-6273-4133-870a-8a70b6f62539", 10L);
        request.put("74c77c10-b6aa-4e04-b520-9b2649e21e6b", 11L);

        CalculatePriceResponse response = new CalculatePriceResponse();
        response.put("c6d6cce5-6273-4133-870a-8a70b6f62539", 99.99);
        response.put("74c77c10-b6aa-4e04-b520-9b2649e21e6b", 89.66);

        //TODO argThat(request::equals) doesn't work correctly
        Mockito.when(productCalculatorService.calculatePrice(argThat(request::equals))).thenReturn(response);

        mockMvc.perform(post("/calculate")
                        .contentType(APPLICATION_JSON)
                        .content(getContent("CalculatePriceRequest.json")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.c6d6cce5-6273-4133-870a-8a70b6f62539", is(99.99)))
                .andExpect(jsonPath("$.74c77c10-b6aa-4e04-b520-9b2649e21e6b", is(89.66)));
    }
}
