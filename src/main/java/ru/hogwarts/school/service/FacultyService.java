package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(long id);

    List<Faculty> getAllFaculties();

    List<Faculty> getFacultiesByColor(String color);

    Faculty changeFaculty(Faculty faculty);

    void deleteFaculty(long id);

    Faculty findFacultyByColorOrName(String color, String name);

    String findLongNameFaculty ();
}
