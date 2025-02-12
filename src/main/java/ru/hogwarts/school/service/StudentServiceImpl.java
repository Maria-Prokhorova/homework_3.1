package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final Map<Long, Student> repositoryStudents = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Student addStudent(Student student) {
        student.setId(++counter);
        return repositoryStudents.put(counter, student);
    }

    @Override
    public Student findStudent(Long id) {
        validataId(id);
        return repositoryStudents.get(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(repositoryStudents.values());
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        validataAge(age);
        return repositoryStudents.entrySet().stream()
                .filter(x -> x.getValue().getAge() == age)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Student changeStudent(Long id, Student student) {
        validataId(id);
        student.setId(id);
        return repositoryStudents.put(id, student);
    }

    @Override
    public Student deleteStudent(Long id) {
        validataId(id);
        return repositoryStudents.remove(id);
    }

    private void validataId(Long id) {
        if (!repositoryStudents.containsKey(id)) {
            throw new NotFoundException("Студента с id = " + " не существует");
        }
    }

    private void validataAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException();
        }
        if (!repositoryStudents.entrySet().stream()
                .anyMatch(x -> x.getValue().getAge() == age)) {
            throw new NotFoundException("Студента с возрастом = " + " не существует");
        }
    }
}
