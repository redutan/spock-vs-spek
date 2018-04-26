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
    val context = createContext(CartServiceSpekIntegTest::class)
    val productRepository = context.inject<ProductRepository>()
    val service = context.inject<CartService>()
    val entityManager = context.inject<EntityManager>()

    fun givenCartItemCreates(products: List<Product>): List<CartItemCreate> {
        return products
                .map({ p -> CartItemCreate(p.productId, 1) })
    }

    describe("addCart") {
        productRepository().deleteAll()
        val products = productRepository().saveAll(listOf(
                Product("TV", 1000000),
                Product("피아노", 3000000),
                Product("색연필", 1000),
                Product("휴대폰", 900000)
        ))
        val persistenceUnitUtil = entityManager().entityManagerFactory.persistenceUnitUtil

        val cartItemCreates = givenCartItemCreates(products)

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