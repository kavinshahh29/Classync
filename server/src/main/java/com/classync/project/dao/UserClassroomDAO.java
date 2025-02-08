package com.classync.project.dao;

import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClassroomDAO extends JpaRepository<UserClassroom, Long> {

    Optional<UserClassroom> findByUserAndClassroom(User user, Classroom classroom);

    List<UserClassroom> findByUser(User user);

    List<UserClassroom> findByClassroom(Classroom classroom);
}