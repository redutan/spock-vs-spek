package io.redutan.spockspek.shop.application

import io.redutan.spockspek.shop.Cart
import io.redutan.spockspek.shop.Product
import io.redutan.spockspek.shop.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest // 부트 통합 공식 지원
class CartServiceSpockIntegTest extends Specification {
    @Autowired  // 타입으로 DI 받기 위해서 def가 아닌 타입을 명시함
    ProductRepository productRepository
    @Autowired
    CartService service
    @PersistenceContext
    EntityManager entityManager
    def persistenceUnitUtil

    List<Product> products

    def setup() {
        productRepository.deleteAll()
        products = productRepository.saveAll([
                new Product("TV", 1_000_000),
                new Product("피아노", 3_000_000),
                new Product("색연필", 1_000),
                new Product("휴대폰", 900_000)
        ])
        // persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        persistenceUnitUtil = entityManager.entityManagerFactory.persistenceUnitUtil
    }

    def "AddCart"() {
        given:
        List<CartItemCreate> cartItemCreates = givenCartItemCreates()

        when:
        Cart result = service.addCart(cartItemCreates)

        then:
        result != null
        result.cartId != null
        result.items.size() == 4
        persistenceUnitUtil.isLoaded(result)
    }

    def givenCartItemCreates() {
        // it = default lambda argument
        return products.collect { new CartItemCreate(it.productId, 1) }
    }
}
