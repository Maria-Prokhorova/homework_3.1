package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty.");
        Faculty saveFaculty = facultyRepository.save(faculty);
        logger.debug("A faculty was created in the database upon request - {}.", saveFaculty);
        return saveFaculty;
    }

    @Override
    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties.");
        return facultyRepository.findAll();
    }

    @Override
    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty by id.");
        validateId(id);
        Faculty findFaculty = facultyRepository.findById(id).get();
        logger.debug("A faculty - {}, was found by id - {}.", findFaculty, id);
        return findFaculty;
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculty by color.");
        validateColor(color);
        List<Faculty> facultyByColor = facultyRepository.findByColor(color);
        logger.debug("Faculties was found by color - {}", facultyByColor);
        return facultyByColor;
    }

    @Override
    public Faculty changeFaculty(Faculty faculty) {
        logger.info("Was invoked method for change faculty.");
        long id = faculty.getId();
        validateId(id);
        logger.debug("New info about faculty is {}.", faculty);
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty by id.");
        validateId(id);
        Faculty findFaculty = facultyRepository.findById(id).get();
        logger.debug("{} was delete.", findFaculty);
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
        logger.info("Was invoked method for find faculty by color or name.");
        validateNameAndColor(name, color);
        return facultyRepository.findByColorIgnoreCaseOrNameContainsIgnoreCase(color, name);
    }

    private void validateId(long id) {
        if (facultyRepository.findById(id).isEmpty()) {
            logger.error("There is not faculty with id = {}", id);
            throw new NotFoundException("Факультета с id = " + id + " не существует");
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (facultyRepository.findByColor(color).isEmpty()) {
            logger.error("There is not faculty with color = {}", color);
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
    }

    private void validateNameAndColor(String name, String color) {
        if ((name == null || name.isBlank()) && (color == null || color.isBlank())) {
            logger.error("None of the parameters are specified");
            throw new IllegalArgumentException("Ни один из обязательных параметров не задан");
        }
        if ((color == null || color.isBlank()) && facultyRepository.findByName(name).isEmpty()) {
            logger.error("There is not faculty with name = {}", name);
            throw new NotFoundException("Факультета с именем - " + name + " не существует");
        }
        if ((name == null || name.isBlank()) && facultyRepository.findByColor(color).isEmpty()) {
            logger.error("There is not faculty with color = {}.", color);
            throw new NotFoundException("Факультета с цветом - " + color + " не существует");
        }
        if (facultyRepository.findByName(name).isEmpty() && facultyRepository.findByColor(color).isEmpty()) {
            logger.error("There is not faculty with color - {} or name - {}.", color, name);
            throw new NotFoundException("Факультета с цветом - " + color + " и именем " + name + " не существует");
        }

    }
}
