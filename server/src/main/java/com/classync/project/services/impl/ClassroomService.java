package com.classync.project.service;

import com.classync.project.dao.ClassroomDAO;
import com.classync.project.entity.Classroom;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassroomService {

    private final ClassroomDAO classroomDAO;

    public ClassroomService(ClassroomDAO classroomDAO) {
        this.classroomDAO = classroomDAO;
    }

    public Classroom createClass(String className) {
        
        Classroom classroom = new Classroom();
        classroom.setClassName(className);

        return classroomDAO.save(classroom);
    }

    public Classroom joinClass(String classroomCode) {
        
        Optional<Classroom> classroomOpt = classroomDAO.findByClassroomCode(classroomCode);

        if (classroomOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid classroom code");
        }

        return classroomOpt.get();
    }
}
