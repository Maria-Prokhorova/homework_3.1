package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }


    @GetMapping("{id}")
    public Student findStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/age")
    public Collection<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("/age-between")
    public Collection<Student> getStudentsByAge(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/idStudent-by-faculty")
    public Faculty findFacultyByStudentId(@RequestParam long studentId) {
        return studentService.findStudent(studentId).getFaculty();
    }

    @PutMapping()
    public Student changeStudent(@RequestBody Student student) {

        return studentService.changeStudent(student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }
}
