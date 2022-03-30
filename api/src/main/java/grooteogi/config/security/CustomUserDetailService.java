package grooteogi.config.security;

import grooteogi.config.cache.CacheKey;
import grooteogi.domain.User;
import grooteogi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = CacheKey.USER, key = "#email", unless = "#result == null")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithAuthority(email).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        return CustomUserDetails.of(user);
    }
}