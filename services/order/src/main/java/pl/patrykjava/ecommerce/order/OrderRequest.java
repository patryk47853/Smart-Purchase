package pl.patrykjava.ecommerce.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import pl.patrykjava.ecommerce.product.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Long id,
        String reference,
        @Positive(message = "Order amount should be positive")
        BigDecimal amount,
        @NotBlank(message = "Payment method should be precised")
        PaymentMethod paymentMethod,
        @NotBlank(message = "Customer must be present")
        String customerId,
        @NotBlank(message = "You should purchase at least one product")
        List<PurchaseRequest> products
) {
}
