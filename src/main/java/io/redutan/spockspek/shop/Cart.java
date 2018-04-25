package io.redutan.spockspek.shop;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Cart implements Serializable {
    @Id
    @GeneratedValue
    private Long cartId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart(List<CartItem> items) {
        this.items = items;
        for (CartItem item : this.items) {
            item.setCart(this);
        }
    }
}
