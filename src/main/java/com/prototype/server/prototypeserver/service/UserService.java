package com.prototype.server.prototypeserver.service;

import com.prototype.server.prototypeserver.entity.Role;
import com.prototype.server.prototypeserver.entity.User;
import com.prototype.server.prototypeserver.repository.RoleRepository;
import com.prototype.server.prototypeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("userService")
public class UserService{

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public void saveNewUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll(new Sort("id"));
    }

}