package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);

    Student findStudent(Long id);

    List<Student> getAllStudents();

    List<Student> getStudentsByAge(int age);

    Student changeStudent(Long id, Student student);

    Student deleteStudent(Long id);
}
