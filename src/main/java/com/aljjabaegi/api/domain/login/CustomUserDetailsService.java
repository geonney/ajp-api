package com.aljjabaegi.api.domain.login;

import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthenticationManagerBuilder 에 의해 호출되며 권한정보를 포함한<br />
 * Security UserDetail 정보를 리턴<br />
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findById(userId).map(member -> createUser(userId, member))
                .orElseThrow(() -> new EntityNotFoundException(userId));
    }

    /**
     * Security User 정보를 리턴
     *
     * @param entity user entity
     * @param userId memberId
     * @return UserDetail
     * @author GEONLEE
     * @since 2024-04-02<br />
     **/
    private User createUser(String userId, Member entity) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(entity.getAuthority().getAuthorityCode()));
        LOGGER.info(userId + " / Authority : {}", grantedAuthorities);
        return new User(
                entity.getMemberId(),
                entity.getPassword(),
                grantedAuthorities
        );
    }
}