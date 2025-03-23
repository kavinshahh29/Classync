package com.classync.project.services.impl;

import com.classync.project.dao.UserDAO;
import com.classync.project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User saveUser(User user) {
        return userDAO.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        if (userDAO.findByEmail(email).isEmpty()) {
            return Optional.empty();
        }
        return userDAO.findByEmail(email);
    }

    public User findById(int id) {
        return userDAO.findById(id).get();
    }

}