package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {

    Student addStudent(Student student);

    Student getStudent(long id);

    List<Student> getAllStudents();

    Student changeStudent(Student student);

    void deleteStudent(long id);

    List<Student> getStudentsByAge(int age);

    List<Student> getStudentsByAgeBetween(int minAge, int maxAge);

    int qetQuantityStudents();

    int getAverageAgeStudent();

    double getAverageAgeStudentByStream();

    List<Student> getLastStudents();

    List<String> getStudentsNameWithA ();

    void getStudentsPrintParallel();

    void getStudentsPrintSynchronized();
}
