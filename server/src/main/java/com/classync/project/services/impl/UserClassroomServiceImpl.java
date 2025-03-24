package com.classync.project.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classync.project.dao.UserDAO;
import com.classync.project.entity.Role;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;
import com.classync.project.repository.RoleRepository;
import com.classync.project.repository.UserClassroomRepository;
import com.classync.project.repository.UserRepository;
import com.classync.project.services.UserClassroomService;

@Service
public class UserClassroomServiceImpl implements UserClassroomService {

    private UserClassroomRepository userClassroomRepository;

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    @Override
    public boolean isUserInClassroom(Long userId, Long classroomId) {
        return userClassroomRepository.existsByUserIdAndClassroomId(userId, classroomId);
    }

    public UserClassroomServiceImpl(UserClassroomRepository userClassroomRepository, RoleRepository roleRepository , UserRepository userRepository) {
        this.userClassroomRepository = userClassroomRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean updateUserRole(int userId, Long classroomId, String newRoleName) {

        UserClassroom userClassroom = userClassroomRepository.findByUserIdAndClassroomId(userId, classroomId)
                .orElseThrow(() -> new RuntimeException("UserClassroom not found"));
        System.out.println(userClassroom);
        Role newRole = roleRepository.findByName(newRoleName);

        userClassroom.setRole(newRole);

        userClassroomRepository.save(userClassroom);
        return true;
    }

    @Transactional
    public boolean unenrollStudent(String useremail, Long classroomId) {
        // Find the user by email
        User user = userRepository.findByEmail(useremail);

        // Find the user-classroom relationship
        Optional<UserClassroom> userClassroomOpt = userClassroomRepository
                .findByUserIdAndClassroomId(user.getId(), classroomId);

        if (!userClassroomOpt.isPresent()) {
            throw new RuntimeException("User is not enrolled in this classroom");
        }

        UserClassroom userClassroom = userClassroomOpt.get();

        // Remove the user from the classroom
        userClassroomRepository.delete(userClassroom);
        return true;
    }

}
