package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTests {

    @Mock
    private StudentRepository repositoryMock;

    @InjectMocks
    private StudentServiceImpl out;

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests")
    public void shouldResultMethodWhenAddStudent(Student student, long correctId, long notCorrectId) {
        when(repositoryMock.save(student)).thenReturn(student);
        assertEquals(student, out.addStudent(student));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests")
    public void shouldResultMethodWhenGetStudentByID(Student student, long correctId, long notCorrectId) {
        when(repositoryMock.findById(correctId)).thenReturn(Optional.of(student));

        assertEquals(student, out.getStudent(correctId));
        assertThrows(NotFoundException.class, () -> out.getStudent(notCorrectId));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    public void shouldResultMethodWhenGetAllStudents(ArrayList<Student> student) {
        when(repositoryMock.findAll()).thenReturn(student);

        assertEquals(student, out.getAllStudents());
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests2")
    public void shouldResultMethodWhenGetStudentByAge(ArrayList<Student> student, int age, int notCorrectAge) {
        when(repositoryMock.findByAge(age)).thenReturn(student);

        assertEquals(student, out.getStudentsByAge(age));
        assertThrows(NotFoundException.class, () -> out.getStudentsByAge(notCorrectAge));
    }

    @Test
    public void shouldResultMethodWhenGetQuantityStudents() {
        int quantity = 10;
        when(repositoryMock.findQuantityStudents()).thenReturn(quantity);

        assertEquals(quantity, out.qetQuantityStudents());
    }

    @Test
    public void shouldResultMethodWhenGetAverageAgeStudent() {
        int averageAge = 20;
        when(repositoryMock.findAverageAgeStudent()).thenReturn(averageAge);

        assertEquals(averageAge, out.getAverageAgeStudent());
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    public void shouldResultMethodWhenGetLastStudents(ArrayList<Student> student) {
        when(repositoryMock.findLastStudents()).thenReturn(student);

        assertEquals(student, out.getLastStudents());
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests")
    public void shouldResultMethodWhenChangeStudent(Student student, long correctId, long notCorrectId) {
        when(repositoryMock.save(student)).thenReturn(student);
        assertEquals(student, out.changeStudent(student));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests")
    void testDeleteStudentWhenException(Student student, long correctId, long notCorrectId) {
        assertThrows(NotFoundException.class, () -> out.deleteStudent(correctId));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests")
    void testDeleteStudent(Student student, long correctId, long notCorrectId) {
        when(repositoryMock.findById(correctId)).thenReturn(Optional.ofNullable(student));
        out.deleteStudent(correctId);
        verify(repositoryMock).deleteById(correctId);
    }

    public static Stream<Arguments> provideCorrectQuestionForTests() {
        return Stream.of(
                Arguments.of(new Student(1, "Maria", 23), 1, 23),
                Arguments.of(new Student(2, "Ira", 35), 2, 35),
                Arguments.of(new Student(3, "Alex", 30), 3, 30)
        );
    }

    public static Stream<Arguments> provideCorrectQuestionForTests1() {
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(
                        new Student(1, "Maria", 20),
                        new Student(2, "Ivan", 25),
                        new Student(3, "Dmitrii", 30)))),
                Arguments.of(new ArrayList<>(List.of(
                        new Student(10, "Ira", 35),
                        new Student(11, "Anna", 27),
                        new Student(12, "Vera", 45)))),
                Arguments.of(new ArrayList<>(List.of(
                        new Student(1, "Alex", 20),
                        new Student(20, "Ira", 33),
                        new Student(12, "Vladimir", 20))))
        );
    }

    public static Stream<Arguments> provideCorrectQuestionForTests2() {
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(
                        new Student(1, "Maria", 25),
                        new Student(2, "Ivan", 25),
                        new Student(3, "Dmitrii", 20))), 25, 10),
                Arguments.of(new ArrayList<>(List.of(
                        new Student(10, "Ira", 35),
                        new Student(11, "Anna", 27),
                        new Student(12, "Vera", 45))), 35, 30),
                Arguments.of(new ArrayList<>(List.of(
                        new Student(1, "Alex", 33),
                        new Student(20, "Ira", 33),
                        new Student(12, "Vladimir", 33))), 33, 25)
        );
    }
}
