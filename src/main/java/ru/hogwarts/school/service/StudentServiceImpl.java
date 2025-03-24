package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public Student addStudent(Student student) {
        logger.info("Was invoked method for create student.");
        Student saveStudent = studentRepository.save(student);
        logger.debug("A student was created in the database upon request - {}.", saveStudent);
        return saveStudent;
    }

    @Override
    public Student getStudent(long id) {
        logger.info("Was invoked method for get student by id.");
        validateId(id);
        Student findStudent = studentRepository.findById(id).get();
        logger.debug("A student - {}, was found by id - {}.", findStudent, id);
        return findStudent;
    }

    @Override
    public List<Student> getAllStudents() {
        logger.info("Was invoked method for get all students.");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age.");
        validateAge(age);
        List<Student> studentsByAge = studentRepository.findByAge(age);
        logger.debug("Students was found by age - {}", studentsByAge);
        return studentsByAge;
    }

    @Override
    public Student changeStudent(Student student) {
        logger.info("Was invoked method for change student.");
        long id = student.getId();
        validateId(id);
        logger.debug("New info about student is {}.", student);
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student by id.");
        validateId(id);
        Student findStudent = studentRepository.findById(id).get();
        logger.debug("{} was delete.", findStudent);
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get students by age between period.");
        validateMinMaxAge(minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public int qetQuantityStudents() {
        logger.info("Was invoked method for get quantity students.");
        int quantityStudents = studentRepository.findQuantityStudents();
        logger.debug("There are {} students in the database.", quantityStudents);
        return quantityStudents;
    }

    @Override
    public int getAverageAgeStudent() {
        logger.info("Was invoked method for get average age student.");
        int averageAgeStudent = studentRepository.findAverageAgeStudent();
        logger.debug("The average age of a student in the database is {}.", averageAgeStudent);
        return averageAgeStudent;
    }

    @Override
    public List<Student> getLastStudents() {
        logger.info("Was invoked method for get 5 last students.");
        return studentRepository.findLastStudents();
    }

    private void validateId(long id) {
        if (studentRepository.findById(id).isEmpty()) {
            logger.error("There is not student with id = {}.", id);
            throw new NotFoundException("Студент с id = " + id + " не существует");
        }
    }

    private void validateAge(int age) {
        if (studentRepository.findByAge(age).isEmpty()) {
            logger.error("There is not student with age = {}.", age);
            throw new NotFoundException("Студента с возрастом - " + age + " не существует");
        }
    }

    private void validateMinMaxAge(int minAge, int maxAge) {
        int min = studentRepository.findMinAge();
        int max = studentRepository.findMaxAge();

        if ((minAge < min && maxAge < min) || (minAge > max && maxAge > max)) {
            logger.error("Invalid age range specified: {} - {}", minAge, maxAge);
            throw new NotFoundException("Неверно задан возрастной диапазон. Студентов с возрастом между "
                    + minAge + " и " + maxAge + " в базе не существует");
        }
    }

}
