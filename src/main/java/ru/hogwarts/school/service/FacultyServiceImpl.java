package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final Map<Long, Faculty> repositoryFaculties = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++counter);
        return repositoryFaculties.put(counter, faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        validataId(id);
        return repositoryFaculties.get(id);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return new ArrayList<>(repositoryFaculties.values());
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        validataColor(color);
        return repositoryFaculties.entrySet().stream()
                .filter(x -> x.getValue().getColor().equals(color))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Faculty changeFaculty(Long id, Faculty faculty) {
        validataId(id);
        faculty.setId(id);
        return repositoryFaculties.put(id, faculty);
    }

    @Override
    public Faculty deleteFaculty(Long id) {
        validataId(id);
        return repositoryFaculties.remove(id);
    }

    private void validataId(Long id) {
        if (!repositoryFaculties.containsKey(id)) {
            throw new NotFoundException("Факультета с id = " + id + " не существует");
        }
    }

    private void validataColor(String color) {
        if (color == null && color.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (!repositoryFaculties.entrySet().stream()
                .anyMatch(x -> x.getValue().getColor().equals(color))) {
            throw new NotFoundException("Факультета с цветом - " + " не существует");
        }
    }
}
