package io.redutan.spockspek.shop.application

import io.redutan.spockspek.shop.Product
import io.redutan.spockspek.shop.ProductRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.spekframework.spring.createContext
import org.springframework.boot.test.context.SpringBootTest
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
object CartServiceSpekIntegTest : Spek({
    // speck-spring 통합은 공식지원되지 않고 있으나, 공식 사이트에 프로젝트는 존재함
    val context = createContext(CartServiceSpekIntegTest::class)
    // productRepository는 bean아니라 Wrapping한 memoize(함수형 캐싱)
    val productRepository = context.inject<ProductRepository>()
    val service = context.inject<CartService>()
    val entityManager = context.inject<EntityManager>()

    describe("addCart") {
        productRepository().deleteAll()
        val products = productRepository().saveAll(listOf(
                Product("TV", 1000000),
                Product("피아노", 3000000),
                Product("색연필", 1000),
                Product("휴대폰", 900000)
        ))
        // persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        val persistenceUnitUtil = entityManager().entityManagerFactory.persistenceUnitUtil

        // it = default lambda argument
        val cartItemCreates = products.map({ CartItemCreate(it.productId, 1) })

        on("장바구니에 상품들 추가") {
            val result = service().addCart(cartItemCreates)

            it("장바구니 정상적으로 생성") {
                assertNotNull(result)
                assertNotNull(result.cartId)
                assertTrue(result.items.size == 4)
                assertTrue(persistenceUnitUtil.isLoaded(result))
            }
        }
    }
}

)