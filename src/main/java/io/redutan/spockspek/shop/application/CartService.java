package io.redutan.spockspek.shop.application;

import io.redutan.spockspek.shop.*;
import io.redutan.spockspek.shop.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart addCart(Collection<CartItemDto> cartItemDtos) {
        Cart cart = createCart(cartItemDtos);
        return cartRepository.save(cart);
    }

    private Cart createCart(Collection<CartItemDto> cartItemDtos) {
        List<CartItem> items = cartItemDtos.stream()
                .map(this::toCartItem)
                .collect(Collectors.toList());
        return new Cart(items);
    }

    private CartItem toCartItem(CartItemDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return new CartItem(product, dto.getQuantity());
    }
}

