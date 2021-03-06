package com.pingconsole.auth.service;


import com.pingconsole.auth.model.Privilege;
import com.pingconsole.auth.model.Role;
import com.pingconsole.auth.model.User;
import com.pingconsole.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("myUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{


    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if(user.getUsername().equalsIgnoreCase("saurabh")){
        	 grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        for (Role role : user.getRoles()){
        	for(Privilege privilege: role.getPrivileges())
            grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getCode()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
