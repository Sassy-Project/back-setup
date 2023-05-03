package com.projectsassy.sassy.token;

import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginId(loginId)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(loginId + "를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(User user) {
        return new PrincipalDetail(user);
    }
}
