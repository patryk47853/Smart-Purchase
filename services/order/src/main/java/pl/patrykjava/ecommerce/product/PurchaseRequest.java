package pl.patrykjava.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(
        @NotNull(message = "Product is mandatory")
        Long productId,
        @Positive(message = "Quantity should be positive")
        double quantity
) {
}
