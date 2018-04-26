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

* junit4 :  
* spock : 
* spek : 

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

### SpringBootTest 