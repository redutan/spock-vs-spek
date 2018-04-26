package io.redutan.spockspek.member

import com.nhaarman.mockito_kotlin.*
import io.github.benas.randombeans.api.EnhancedRandom.random
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

// object = singleton
object MemberServiceSpec : Spek({
    // void describe(String, Supplier<SpecBody>)
    // describe("", () -> { })
    // describe("", { })
    // describe("") { }
    // Given : 문자열로 표현 가능
    describe("인증 주체 조회 테스트") {
        // val = value = immutable
        // var = variant = mutable
        // val memberRepository: MemberRepository? > nullable
        // val memberRepository: MemberRepository > non-nullable
        val memberRepository: MemberRepository = mock()
        // MemberService service = new MemberService(memberRepository);
        val service = MemberService(memberRepository)

        val username = "userId"
        val role1 = "USER1"
        // @Before
        beforeEachTest {
            reset(memberRepository)
        }
        // When : 또한 문자열로 표현 가능
        on("정상 조회 시") {
            val member = Member(username, random(String::class.java), role1)
            // when stubbing
            whenever(memberRepository.findById(username)).thenReturn(Optional.of(member))

            val user = service.loadUserByUsername(username)
            // Then
            it("정상적으로 조회 성공되어야함") {
                assertNotNull(user)
                assertEquals(user.username, username)
                assertNotNull(user.password)
                assertEquals(user.authorities.first().authority, "ROLE_$role1")
            }
            // Then : 또한 문자열로 표현 가능
            it("하위 모듈이 정상적으로 호출되어야함") {
                verify(memberRepository, times(1)).findById(username)
            }
        }
        on("회원이 미존재 시") {
            whenever(memberRepository.findById(username)).thenReturn(Optional.empty())

            it("회원을 찾을 수 없는 예외 발생") {
                assertFailsWith(UsernameNotFoundException::class) {
                    service.loadUserByUsername(username)
                }
            }
            it("하위 모듈이 정상적으로 호출되어야함") {
                verify(memberRepository, times(1)).findById(username)
            }
        }
        on("회원의 권한이 없을 시") {
            val noAuthorityMember = Member(username, random(String::class.java), null)
            whenever(memberRepository.findById(username)).thenReturn(Optional.of(noAuthorityMember))

            it("회원을 찾을 수 없는 예외 발생") {
                assertFailsWith(UsernameNotFoundException::class) {
                    service.loadUserByUsername(username)
                }
            }
            it("하위 모듈이 정상적으로 호출되어야함") {
                verify(memberRepository, times(1)).findById(username)
            }
        }
    }
})
