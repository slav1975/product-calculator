package pl.reactive11.product_calculator.product;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ProductRepository extends ListCrudRepository<ProductEntity, UUID> {}
