package io.redutan.spockspek.shop;

import io.redutan.spockspek.refund.Order;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.NONE)
public class CartItem implements Serializable {
    @Id
    @GeneratedValue
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @Setter(AccessLevel.PACKAGE)
    private Cart cart;

    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
