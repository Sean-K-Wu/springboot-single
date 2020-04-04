package cn.wuxiangknow.single.common.dto.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(String id, String username, String password, Collection<? extends GrantedAuthority> authorities, Date lastLoginDate, Date lastPasswordResetDate) {
        final  JwtUser jwtUser = new JwtUser(id, username, password, authorities,lastLoginDate,lastPasswordResetDate);
        return jwtUser;
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
