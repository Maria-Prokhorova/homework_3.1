package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpMethod.GET;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void whenPostStudent_thenStatus200() {
        Student student = new Student();
        student.setName("math");
        student.setAge(29);
        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(student.getName());
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void whenGetByIdStudent_thenStatusOk() {
        Student student = new Student();
        student.setName("Ivan");
        student.setAge(20);
        ResponseEntity<Student> responseAddStudent = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(responseAddStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        int id = (int) requireNonNull(responseAddStudent.getBody()).getId();

        ResponseEntity<Student> responseGetStudent = restTemplate.getForEntity("http://localhost:" + port + "/student/{id}", Student.class, id);
        Assertions.assertThat(responseGetStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetStudent.getBody().getName()).isEqualTo(student.getName());
        Assertions.assertThat(responseGetStudent.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void whenGetByIdStudent_thenStatusNotFound() {
        final long id = 1000;
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/student/{id}", String.class, id);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetByAgeStudent_thenStatusOk() {
        Student student = new Student();
        student.setName("law1");
        student.setAge(18);
        ResponseEntity<Student> responseAddStudent = restTemplate.postForEntity("http://localhost:"
                + port + "/student", student, Student.class);
        ResponseEntity<String> responseGetStudent = restTemplate.getForEntity("http://localhost:"
                + port + "/student/age?age=" + student.getAge(), String.class);
        Assertions.assertThat(responseGetStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenGetByAgeStudent_thenStatusNotFound() {
        int age = 22222;
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"
                + port + "/student/age?age=" + age, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetByAgeBetweenStudent_thenStatusOk() {
        Student student1 = studentController.addStudent(new Student(0L, "roy", 20));
        Student student2 = studentController.addStudent(new Student(0L, "ret", 25));

        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:"
                + port + "/student/age-between?min=" + 18 + "&max=" + 30, Student[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains(student1);
        Assertions.assertThat(response.getBody()).contains(student2);
    }

    @Test
    public void whenGetByAgeBetweenStudent_thenStatusNotFound() {
        Student student1 = studentController.addStudent(new Student(0L, "Ann", 30));
        Student student2 = studentController.addStudent(new Student(0L, "Irina", 27));

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"
                + port + "/student/age-between?min=" + 35 + "&max=" + 40, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetAllStudents() {

        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student", GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().size()).isNotNull();
    }

    @Test
    public void whenGetFacultyByStudentId_thenStatusOk() {
        Faculty faculty1 = facultyController.addFaculty(new Faculty(0L, "music", "green"));
        Student student1 = studentController.addStudent(new Student(0L, "Vova", 37));
        student1.setFaculty(faculty1);
        studentController.changeStudent(student1);

        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:"
                        + port + "/student/idStudent-by-faculty?id=" + student1.getId(), GET, null,
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().equals(faculty1)).isTrue();
    }

    @Test
    public void whenGetFacultyByStudentId_thenStatusNotFound() {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:"
                        + port + "/student/idStudent-by-faculty?id=" + 1000, GET, null,
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetQuantityStudents() {
        int quantity = 11;
        ResponseEntity<Integer> responseGetStudent = restTemplate.getForEntity("http://localhost:" + port + "/student/quantity-students", Integer.class);
        Assertions.assertThat(responseGetStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetStudent.getBody()).isEqualTo(quantity);
    }

    @Test
    public void whenPutStudent_thenStatusOk() {
        Student studentForAdd = new Student();
        studentForAdd.setName("r3de3dg5gfqdewe");
        studentForAdd.setAge(18);
        ResponseEntity<Student> responseAddStudent = restTemplate.postForEntity("http://localhost:" + port + "/student", studentForAdd, Student.class);
        int id = (int) requireNonNull(responseAddStudent.getBody()).getId();

        Student studentForChange = new Student();
        studentForChange.setId(id);
        studentForChange.setName("Maria");
        studentForChange.setAge(18);
        ResponseEntity<Void> responseChangeStudent = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT, new HttpEntity<>(studentForChange), Void.class);
        Assertions.assertThat(responseChangeStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenPutStudent_thenStatusNotFound() {
        Student studentForChange = new Student();
        studentForChange.setId(1000);
        studentForChange.setName("rush");
        studentForChange.setAge(30);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT, new HttpEntity<>(studentForChange), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenDeleteStudent() {
        Student student = new Student();
        student.setName("Malfoi");
        student.setAge(25);

        ResponseEntity<Student> responseAddStudent = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        long id = requireNonNull(responseAddStudent.getBody()).getId();

        ResponseEntity<Student> responseFindStudent = restTemplate.getForEntity("http://localhost:" + port + "/student/" + id, Student.class);
        Assertions.assertThat(responseFindStudent.getStatusCode()).isEqualTo(HttpStatus.OK);

        restTemplate.delete("http://localhost:" + port + "/student/" + id);

        ResponseEntity<String> responseCheckStudent = restTemplate.getForEntity("http://localhost:" + port + "/student/" + id, String.class);
        Assertions.assertThat(responseCheckStudent.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
