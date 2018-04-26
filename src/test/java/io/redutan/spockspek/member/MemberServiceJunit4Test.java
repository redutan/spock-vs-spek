package io.redutan.spockspek.member;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("WeakerAccess")
@RunWith(MockitoJUnitRunner.class)
public class MemberServiceJunit4Test {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    MemberService service;
    @Mock
    MemberRepository memberRepository;

    final String username = "userId";
    final String role1 = "USER1";

    @Test
    public void loadUserByUsername() {
        // given
        Member member = new Member(username, random(String.class), role1);
        when(memberRepository.findById(username)).thenReturn(Optional.of(member));
        // when
        UserDetails userDetails = service.loadUserByUsername(username);
        // then
        assertThat(userDetails.getUsername(), is(username));
        assertThat(userDetails.getPassword(), is(notNullValue()));
        assertThat(AuthorityUtils.authorityListToSet(userDetails.getAuthorities()),
                is(AuthorityUtils.authorityListToSet(AuthorityUtils.createAuthorityList("ROLE_" + role1))));
        verify(memberRepository, times(1)).findById(username);
    }

    @Test
    public void loadUserByUsername_notFoundMember() {
        // given
        expectedException.expect(UsernameNotFoundException.class);
        final String username = "userId";

        when(memberRepository.findById(username)).thenReturn(Optional.empty());
        // when
        service.loadUserByUsername(username);
        // then
        verify(memberRepository, times(1)).findById(username);
    }

    @Test
    public void loadUserByUsername_noAuthority() {
        // given
        expectedException.expect(UsernameNotFoundException.class);
        final String username = "userId";

        Member noAuthorityMember = new Member(username, random(String.class), null);
        when(memberRepository.findById(username)).thenReturn(Optional.of(noAuthorityMember));
        // when
        service.loadUserByUsername(username);
        // then
        verify(memberRepository, times(1)).findById(username);
    }
}