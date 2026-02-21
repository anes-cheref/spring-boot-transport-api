package com.transport.transport_api.security;

import com.transport.transport_api.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phone_number) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phone_number).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
