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
    public Student getStudent(@PathVariable long id) {
        return studentService.getStudent(id);
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
    public Collection<Student> getStudentsByAge(@RequestParam (name = "min") int minAge, @RequestParam (name= "max") int maxAge) {
        return studentService.getStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/idStudent-by-faculty")
    public Faculty getFacultyByStudentId(@RequestParam (name = "id") long studentId) {
        return studentService.getStudent(studentId).getFaculty();
    }

    @GetMapping("/quantity-students")
    public int getQuantityStudents () {
        return studentService.qetQuantityStudents();
    }

    @GetMapping ("/average-age")
    public int getAverageAgeStudent () {
        return studentService.getAverageAgeStudent();
    }

    @GetMapping ("/average-age-by-Stream")
    public double getAverageAgeStudentByStream () {
        return studentService.getAverageAgeStudentByStream();
    }

    @GetMapping ("/last-students")
    public Collection <Student> getLastStudents () {
        return studentService.getLastStudents();
    }

    @GetMapping ("/name-with-a")
    public Collection <String> getStudentsNameWithA () {
        return studentService.getStudentsNameWithA();
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
