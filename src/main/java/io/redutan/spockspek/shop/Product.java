package io.redutan.spockspek.shop;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Product implements Serializable {
    @Id
    @GeneratedValue
    private Long productId;
    private String name;
    private long price;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }
}
