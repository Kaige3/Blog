//package com.kaige.entity.vo;
//
//import com.kaige.entity.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class UserDetailsImpl implements UserDetails {
//
//    private final BigInteger id;
//    private final String username;
//    private final String password;
//    private final String nickname;
//    private final String avatar;
//    private final String email;
//    private final String role;
//
//    public UserDetailsImpl(User user) {
//        this.id = user.id();
//        this.username = user.username();
//        this.password = user.password();
//        this.nickname = user.nickname();
//        this.avatar = user.avatar();
//        this.email = user.email();
//        this.role = user.role();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 返回用户的角色权限，Spring Security会根据这些权限控制访问
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(role));
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;  // 账户未过期
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;  // 账户未锁定
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;  // 凭证未过期（通常是密码）
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;  // 账户启用
//    }
//
//    // 如果需要更多用户信息，可以在这里添加
//    public BigInteger getId() {
//        return id;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//}
