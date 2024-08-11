package org.example.newstudent.DAO;

import org.example.newstudent.Dto.StudentDTO;

import java.sql.Connection;

public sealed interface Data permits DataProcess {
    boolean save(StudentDTO studentDTO, Connection connection);

    boolean update(String studentId, StudentDTO updateStudentDTO, Connection connection);

    StudentDTO delete(String studentId, Connection connection);

    StudentDTO get(String studentId, Connection connection);
}
