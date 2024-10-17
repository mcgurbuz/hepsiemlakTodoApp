package com.hepsiemlak.todo.service;

import com.hepsiemlak.todo.dto.request.RegisterRequest;
import com.hepsiemlak.todo.exception.ConflictException;
import com.hepsiemlak.todo.exception.ResourceNotFoundException;
import com.hepsiemlak.todo.exception.message.ErrorMessage;
import com.hepsiemlak.todo.model.Role;
import com.hepsiemlak.todo.repository.RoleRepository;
import com.hepsiemlak.todo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hepsiemlak.todo.model.User;

import java.util.*;


@Service
@AllArgsConstructor
public class UserService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new
                    ConflictException(
                            String.format(ErrorMessage.EMAIL_ALREADY_EXIST,registerRequest.getEmail()));
        }

        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new
                    ConflictException(
                    String.format(ErrorMessage.USERNAME_ALREADY_EXIST,registerRequest.getUsername()));
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        Role customerRole = new Role();
        customerRole.setId(UUID.randomUUID().toString());
        customerRole.setRoleName("CUSTOMER");
        customerRole.setDescription("Customer role with basic permissions");

        roleRepository.save(customerRole);


        List<Role> customerRoles = roleRepository.findByRoleName("CUSTOMER");

        if (customerRoles.isEmpty()) {
            throw new ResourceNotFoundException("CUSTOMER role not found.");
        }

        Role role = customerRoles.get(0);  // İlk bulunan rolü al
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodedPassword);
        user.setRoles(roles);

        roleRepository.save(role);
        userRepository.save(user);

    }

    public User getOneUserByUsername(String username){
        User user = userRepository.findByUsername(username).
                orElseThrow(()->new UsernameNotFoundException(
                        String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE,username)
                ));

        return user;
    }


}
