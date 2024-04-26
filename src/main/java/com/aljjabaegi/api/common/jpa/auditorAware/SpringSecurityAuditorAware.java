package com.aljjabaegi.api.common.jpa.auditorAware;

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
 * @since 2024-04-08<br />
 * 2024-04-26 GEONLEE 권한이 없는 익명 사용자 처리 추가, anonymousUser 조건 추가<br />
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    @Nonnull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        return Optional.of(((UserDetails) authentication.getPrincipal()).getUsername());
    }
}
