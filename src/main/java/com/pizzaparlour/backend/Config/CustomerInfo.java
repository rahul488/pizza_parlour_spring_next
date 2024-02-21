package com.pizzaparlour.backend.Config;

import com.pizzaparlour.backend.Entity.Customer;
import com.pizzaparlour.backend.Repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerInfo implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> userInfo = customerRepo.findByEmail(username);
        return userInfo.map(CustomerDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }
}
