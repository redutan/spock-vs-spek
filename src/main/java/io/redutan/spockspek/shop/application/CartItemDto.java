package io.redutan.spockspek.shop.application;

import lombok.NonNull;
import lombok.Value;

@Value
class CartItemDto {
    @NonNull
    private Long productId;
    private int quantity;
}
