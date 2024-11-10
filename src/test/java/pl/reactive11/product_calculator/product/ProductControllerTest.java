package pl.reactive11.product_calculator.product;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.reactive11.product_calculator.TestContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.reactive11.product_calculator.TestUtils.getContent;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest extends TestContext {

    @MockBean
    ProductRepository repository;

    @Test
    void shouldCreateProductList() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(APPLICATION_JSON)
                        .content(getContent("CreateProductRequestList.json")))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldFindAllProducts() throws Exception {

        Mockito.when(repository.findAll())
                .thenReturn(List.of(ProductEntity
                        .builder()
                        .id(UUID.randomUUID())
                        .name("NAME")
                        .price(BigDecimal.ONE)
                        .build()));
        mockMvc.perform(get("/products")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}