package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query("SELECT MIN(s.age) FROM Student s")
    int findMinAge();

    @Query("SELECT MAX(s.age) FROM Student s")
    int findMaxAge();

    @Query(value = "SELECT COUNT(*) FROM Student", nativeQuery = true)
    int findQuantityStudents();

    @Query(value = "SELECT AVG(s.age) FROM Student s", nativeQuery = true)
    int findAverageAgeStudent();

    @Query(value = "SELECT * FROM Student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List <Student> findLastStudents ();
}
