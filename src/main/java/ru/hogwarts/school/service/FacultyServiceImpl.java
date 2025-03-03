package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

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
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty findFaculty(long id) {
        validateId(id);
        return facultyRepository.findById(id).get();
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        validateColor(color);
        return facultyRepository.findByColor(color);
    }

    @Override
    public Faculty changeFaculty(Faculty faculty) {
        long id = faculty.getId();
        validateId(id);
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        validateId(id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
        validateNameAndColor(name, color);
        return facultyRepository.findByColorIgnoreCaseOrNameContainsIgnoreCase(color, name);
    }

    private void validateId(long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Факультета с id = " + id + " не существует");
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (facultyRepository.findByColor(color).isEmpty()) {
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
    }

    private void validateNameAndColor(String name, String color) {
        if ((name == null || name.isBlank()) && (color == null || color.isBlank())) {
            throw new IllegalArgumentException("Ни один из обязательных параметров не задан");
        }
        if ((color == null || color.isBlank()) && facultyRepository.findByName(name).isEmpty()) {
            throw new NotFoundException("Факультета с именем - " + name + " не существует");
        }
        if ((name == null || name.isBlank()) && facultyRepository.findByColor(color).isEmpty()) {
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
        if (facultyRepository.findByName(name).isEmpty() && facultyRepository.findByColor(color).isEmpty()) {
            throw new NotFoundException("Факультета с цветом - " + color + " и именем " + name + " не существует");
        }

    }
}
