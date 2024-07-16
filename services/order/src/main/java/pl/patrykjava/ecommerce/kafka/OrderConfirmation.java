package pl.patrykjava.ecommerce.kafka;

import pl.patrykjava.ecommerce.customer.CustomerResponse;
import pl.patrykjava.ecommerce.order.PaymentMethod;
import pl.patrykjava.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
