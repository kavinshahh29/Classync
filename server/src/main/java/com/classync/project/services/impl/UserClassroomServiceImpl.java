package com.classync.project.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.classync.project.entity.Role;
import com.classync.project.entity.UserClassroom;
import com.classync.project.repository.RoleRepository;
import com.classync.project.repository.UserClassroomRepository;
import com.classync.project.services.UserClassroomService;

@Service
public class UserClassroomServiceImpl implements UserClassroomService {

    private UserClassroomRepository userClassroomRepository;

    private RoleRepository roleRepository;

    @Override
    public boolean isUserInClassroom(Long userId, Long classroomId) {
        return userClassroomRepository.existsByUserIdAndClassroomId(userId, classroomId);
    }
    
    public UserClassroomServiceImpl(UserClassroomRepository userClassroomRepository, RoleRepository roleRepository) {
        this.userClassroomRepository = userClassroomRepository;
        this.roleRepository = roleRepository;
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

}
