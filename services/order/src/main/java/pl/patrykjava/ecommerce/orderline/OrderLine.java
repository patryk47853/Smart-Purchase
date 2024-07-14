package pl.patrykjava.ecommerce.orderline;

import jakarta.persistence.*;
import lombok.*;
import pl.patrykjava.ecommerce.order.Order;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class OrderLine {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long productId;

    private double quantity;
}
