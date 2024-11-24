package com.ng.bsa.configuration;

import com.ng.bsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        var user =
                userRepository.findByEmail(username).orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User does not exist"));
        return new com.ng.bsa.configuration.UserDetails(user);
    }
}
