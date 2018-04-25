package io.redutan.spockspek.shop.application;

import io.redutan.spockspek.shop.Cart;
import io.redutan.spockspek.shop.Product;
import io.redutan.spockspek.shop.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService service;
    @PersistenceContext
    EntityManager entityManager;
    PersistenceUnitUtil persistenceUnitUtil;

    List<Product> products;

    @Before
    public void setUp() throws Exception {
        productRepository.deleteAll();
        products = productRepository.saveAll(Arrays.asList(
                new Product("TV", 1_000_000),
                new Product("피아노", 3_000_000),
                new Product("색연필", 1_000),
                new Product("휴대폰", 900_000)
        ));
        persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
    }

    @Test
    public void addCart() {
        // given
        List<CartItemDto> cartItemDtos = givenCartItemDtos();
        // when
        Cart result = service.addCart(cartItemDtos);
        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getCartId(), is(notNullValue()));
        persistenceUnitUtil.isLoaded(result);
    }

    private List<CartItemDto> givenCartItemDtos() {
        return products.stream()
                .map(p -> new CartItemDto(p.getProductId(), 1))
                .collect(Collectors.toList());
    }

//    @After
//    public void tearDown() throws Exception {
//        productRepository.deleteAll();
//    }
}