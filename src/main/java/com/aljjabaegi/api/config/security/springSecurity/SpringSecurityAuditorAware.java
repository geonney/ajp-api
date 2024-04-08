package com.aljjabaegi.api.config.security.springSecurity;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring security 의 UserDetail 을 추출해 영속화 시 값을 바인딩 할 수 있게 한다.<br />
 * LastModifiedBy annotation 사용 시 MemberId 자동 바인딩
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    @Nonnull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(((UserDetails) authentication.getPrincipal()).getUsername());
    }
}
