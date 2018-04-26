package io.redutan.spockspek.shop.application;

import io.redutan.spockspek.shop.Cart;
import io.redutan.spockspek.shop.Product;
import io.redutan.spockspek.shop.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceJunit4IntegTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService service;
    @PersistenceContext
    EntityManager entityManager;
    PersistenceUnitUtil persistenceUnitUtil;

    List<Product> products;

    @Before
    public void setUp() {
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
        List<CartItemCreate> cartItemCreates = givenCartItemCreates();
        // when
        Cart result = service.addCart(cartItemCreates);
        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getCartId(), is(notNullValue()));
        assertThat(result.getItems(), hasSize(4));
        assertTrue(persistenceUnitUtil.isLoaded(result));
    }

    private List<CartItemCreate> givenCartItemCreates() {
        return products.stream()
                .map(p -> new CartItemCreate(p.getProductId(), 1))
                .collect(Collectors.toList());
    }

//    @After
//    public void tearDown() throws Exception {
//        productRepository.deleteAll();
//    }
}