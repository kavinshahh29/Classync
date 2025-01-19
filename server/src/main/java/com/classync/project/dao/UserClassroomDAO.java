package com.classync.project.dao;

import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

public interface UserClassroomDAO extends JpaRepository<UserClassroom, Long> {

    Optional<UserClassroom> findByUserAndClassroom(User user, Classroom classroom);

}
