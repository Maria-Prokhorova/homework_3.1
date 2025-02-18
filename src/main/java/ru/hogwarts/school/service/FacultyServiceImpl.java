package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(long id) {
        validateId(id);
        return facultyRepository.findById(id).get();
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        validateColor(color);
        return facultyRepository.findByColor(color);
    }

    @Override
    public Faculty changeFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        validateId(id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
       return facultyRepository.findByColorIgnoreCaseOrNameContainsIgnoreCase(color, name);
    }

    private void validateId(long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Факультета с id = " + id + " не существует");
        }
    }

    private void validateColor(String color) {
        if (color == null && color.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (facultyRepository.findByColor(color).isEmpty()) {
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
    }
}
