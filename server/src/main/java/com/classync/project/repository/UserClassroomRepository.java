package com.classync.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;
import java.util.List;


@Repository
public interface UserClassroomRepository extends JpaRepository<UserClassroom, Long> {
    Optional<UserClassroom> findByUserIdAndClassroomId(int userId, Long classroomId);

    boolean existsByUserIdAndClassroomId(Long userId, Long classroomId);

    List<Classroom> findClassroomsByUser(User user);
}
