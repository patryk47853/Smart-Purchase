package pl.patrykjava.ecommerce.order;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.patrykjava.ecommerce.customer.CustomerClient;
import pl.patrykjava.ecommerce.exception.BusinessException;
import pl.patrykjava.ecommerce.kafka.OrderConfirmation;
import pl.patrykjava.ecommerce.kafka.OrderProducer;
import pl.patrykjava.ecommerce.orderline.OrderLineRequest;
import pl.patrykjava.ecommerce.orderline.OrderLineService;
import pl.patrykjava.ecommerce.product.ProductClient;
import pl.patrykjava.ecommerce.product.PurchaseRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Long createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Can't create order - No Customer with ID: " + request.customerId() + " exists in database."));

        var purchasedProducts = productClient.purchaseProducts(request.products());

        var order = orderRepository.save(orderMapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //TODO: Payment (payment-ms)

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("No order found with ID: " + orderId));
    }
}
