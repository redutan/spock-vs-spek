package io.redutan.spockspek.member

import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

import static io.github.benas.randombeans.api.EnhancedRandom.random

class MemberServiceSpockTest extends Specification {
    def service = new MemberService()
    def memberRepository

    def username = "userId"
    def role1 = "USER1"

    // @Before
    def setup() {
        memberRepository = Mock(MemberRepository)
        service = new MemberService(memberRepository)
    }

    def "LoadUserByUsername"() {
        given:
        def member = new Member(username, random(String.class), role1)

        when:
        def userDetails = service.loadUserByUsername(username)

        then:
        // verify + when(memberRepository.findById(username).thenReturn(Optional.empty())
        memberRepository.findById(username) >> Optional.of(member)
        userDetails.username == username
        userDetails.password != null
        userDetails.authorities.first().authority == "ROLE_${role1}"
    }

    def "LoadUserByUsername - Not found member"() {
        when:
        service.loadUserByUsername(username)

        then:
        // verify 1 times + when(memberRepository.findById(any())).thenReturn(Optional.empty())
        1 * memberRepository.findById(_) >> Optional.empty()
        thrown UsernameNotFoundException
    }

    def "LoadUserByUsername - No authority"() {
        given:
        def noAuthorityMember = new Member(username, random(String.class), null)

        when:
        service.loadUserByUsername(username)

        then:
        1 * memberRepository.findById(_) >> Optional.of(noAuthorityMember)
        thrown UsernameNotFoundException
    }
}
