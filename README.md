# spock-vs-spek

## 동기

최근 프로젝트를 진행함에 있어서 `kotlin`으로 진행하고 싶은 욕심이 있었습니다. 하지만 그와 관련해서 조직장을 설들하기가 매우 힘들었습니다.
그러던 중 팀 내에 `kotlin`의 장점을 이야기 하면서 `spek` 이라는 꽤 괜찮은 `kotlin` 기반 테스트 프레임워크에 대한 이야기를 하였으며, 
추가적으로 간결함을 추구하는 `groovy`기반의 `spock`도 이야기 하였습니다.

*프로덕션 코드가 아니라 테스트 코드라도 `kotlin`으로 진행하고 싶었습니다.*

그런데 팀원 중 한 분께서 그러면 한 번 **각 테스트 프레임워크를 비교하는 세미나**를 하는 것이 어떠냐고 하셔서 이렇게 준비하게 되었습니다.

> 이 문서를 `@부릉` 에게 바칩니다.

## 가이드

코드를 기준으로 각 프레임워크를 비교합니다.

`junit4`, `spock`, `speck` 순으로 확인합니다.

> Talk is cheap. Show me the code - linus torvalds

## Examples

### Basic

*MemberService.java*

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("Not found member : " + username));
    String memberAuthority = Optional.ofNullable(member.getAuthority())
            .orElseThrow(() -> new UsernameNotFoundException("Not found authority : " + member));
    return User.withUsername(username).password(member.getPassword()).roles(memberAuthority).build();
}
```

**Test**
* junit4 : [MemberServiceJunit4Test.java](src/test/java/io/redutan/spockspek/member/MemberServiceJunit4Test.java)
* spock : [MemberServiceSpockTest.groovy](src/test/java/io/redutan/spockspek/member/MemberServiceSpockTest.groovy)
* spek : [MemberServiceSpekTest.kt](src/test/java/io/redutan/spockspek/member/MemberServiceSpekTest.kt)

### Parameterized

*RefundService.java*

```java
public long getRefundFee(Order order) {
    long amount = order.getAmount();
    if (amount < 10000L)    // 0%
        return 0;
    else if (amount < 50000L)   // 10%
        return amount * 10 / 100;
    else    // 20%
        return amount * 20 / 100;
}
```

**Test**
* junit4 : [RefundServiceJunit4Test.java](src/test/java/io/redutan/spockspek/refund/RefundServiceJunit4Test.java)
* spock : [RefundServiceSpockTest.groovy](src/test/java/io/redutan/spockspek/refund/RefundServiceSpockTest.groovy)
* spek : [RefundServiceSpekTest.kt](src/test/java/io/redutan/spockspek/refund/RefundServiceSpekTest.kt)

### SpringBootTest

*CartService.java*

```java
@Transactional
public Cart addCart(Collection<CartItemCreate> cartItemCreates) {
    Cart cart = createCart(cartItemCreates);
    return cartRepository.save(cart);
}
private Cart createCart(Collection<CartItemCreate> cartItemCreates) {
    List<CartItem> items = cartItemCreates.stream()
            .map(this::toCartItem)
            .collect(Collectors.toList());
    return new Cart(items);
}
private CartItem toCartItem(CartItemCreate dto) {
    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(ProductNotFoundException::new);
    return new CartItem(product, dto.getQuantity());
}
```

**Test**
* junit4 : [CartServiceJunit4IntegTest.java](src/test/java/io/redutan/spockspek/shop/application/CartServiceJunit4IntegTest.java)
* spock : [CartServiceSpockIntegTest.groovy](src/test/java/io/redutan/spockspek/shop/application/CartServiceSpockIntegTest.groovy)
* spek : [CartServiceSpekIntegTest.kt](src/test/java/io/redutan/spockspek/shop/application/CartServiceSpekIntegTest.kt)

## Summary

* `spek`가 생각보다 아쉬웠습니다. 표현과 설명은 훌륭하나 테스트 작성의 시간이 Junit에 비해서 빠르지 않습니다.
    * 추가적으로 `spek`는 Spring 통합이나 자체 지원도 아쉬운 편이였으며, 작년(2017) 이후 업데이트도 없습니다.
    * 현재 `spek2` 가 준비 중인 것으로 확인되니 거기에 기대를 해봅니다. 
* 오히려 `spock`가 더 나은 것 같습니다.
    * 원래 세미나를 준비한 것은 `spek`를 염두해 둔 것이었는데, 속도, 지원(통합), 가독성 등 **대부분의 면에서 `spock`가 나은 것 같습니다.**
    * 물론 `spock`을 잘 사용하기 위해서는 `groovy`를 더 공부해야하지만 현 상황으로도 충분해 보였습니다.