package ru.hogwarts.school;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceImplTests {
    @Mock
    private FacultyRepository repositoryMock;

    @InjectMocks
    private FacultyServiceImpl out;

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    public void shouldResultMethodWhenAddFaculty(Faculty faculty, long correctId, long notCorrectId) {
        when(repositoryMock.save(faculty)).thenReturn(faculty);
        assertEquals(faculty, out.addFaculty(faculty));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    public void shouldResultMethodWhenFindFaculty(Faculty faculty, long correctId, long notCorrectId) {
        when(repositoryMock.findById(correctId)).thenReturn(Optional.of(faculty));

        assertEquals(faculty, out.findFaculty(correctId));
        assertThrows(NotFoundException.class, () -> out.findFaculty(notCorrectId));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests2")
    public void shouldResultMethodWhenGetAllFaculties(ArrayList<Faculty> faculties) {
        when(repositoryMock.findAll()).thenReturn(faculties);

        assertEquals(faculties, out.getAllFaculties());
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests2")
    public void shouldResultMethodWhenGetFacultyByColor(ArrayList<Faculty> faculties, String color, String notCorrectColor) {
        when(repositoryMock.findByColor(color)).thenReturn(faculties);

        assertEquals(faculties, out.getFacultiesByColor(color));
        assertThrows(NotFoundException.class, () -> out.getFacultiesByColor(notCorrectColor));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    public void shouldResultMethodWhenChangeFaculty(Faculty faculty, long correctId, long notCorrectId) {
        when(repositoryMock.save(faculty)).thenReturn(faculty);
        assertEquals(faculty, out.changeFaculty(faculty));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    void testDeleteFacultyWhenException(Faculty faculty, long correctId, long notCorrectId) {
        assertThrows(NotFoundException.class, () -> out.deleteFaculty(correctId));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectQuestionForTests1")
    void testDeleteFaculty(Faculty faculty, long correctId, long notCorrectId) {
        when(repositoryMock.findById(correctId)).thenReturn(Optional.ofNullable(faculty));

        out.deleteFaculty(correctId);
        verify(repositoryMock).deleteById(correctId);
    }

    public static Stream<Arguments> provideCorrectQuestionForTests1() {
        return Stream.of(
                Arguments.of(new Faculty(1, "Maria", "red"), 1, 23),
                Arguments.of(new Faculty(2, "Ira", "green"), 2, 35),
                Arguments.of(new Faculty(3, "Alex", "white"), 3, 30)
        );
    }

    public static Stream<Arguments> provideCorrectQuestionForTests2() {
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(
                        new Faculty(1, "Maria", "red"),
                        new Faculty(2, "Ivan", "green"),
                        new Faculty(3, "Dmitrii", "red"))), "red", "10"),
                Arguments.of(new ArrayList<>(List.of(
                        new Faculty(10, "Ira", "35"),
                        new Faculty(11, "Anna", "27"),
                        new Faculty(12, "Vera", "45"))), "35", "30"),
                Arguments.of(new ArrayList<>(List.of(
                        new Faculty(1, "Alex", "33"),
                        new Faculty(20, "Ira", "33"),
                        new Faculty(12, "Vladimir", "33"))), "33", "25")
        );
    }

}
