package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    List<Faculty> getAllFaculties();

    List<Faculty> getFacultiesByColor(String color);

    Faculty changeFaculty(Long id, Faculty faculty);

    Faculty deleteFaculty(Long id);
}
