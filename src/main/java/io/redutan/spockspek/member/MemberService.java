package io.redutan.spockspek.member;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found member : " + username));
        String memberAuthority = Optional.ofNullable(member.getAuthority())
                .orElseThrow(() -> new UsernameNotFoundException("Not found authority : " + member));
        return User.withUsername(username).password(member.getPassword()).roles(memberAuthority).build();
    }
}
