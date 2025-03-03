package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpMethod.GET;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void whenPostFaculty_thenStatus200() {
        Faculty faculty = new Faculty();
        faculty.setName("math");
        faculty.setColor("green");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void whenGetByIdFaculty_thenStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("law");
        faculty.setColor("red");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(responseAddFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        long id = requireNonNull(responseAddFaculty.getBody()).getId();

        ResponseEntity<Faculty> responseGetFaculty = restTemplate.getForEntity("http://localhost:" + port + "/faculty/{id}", Faculty.class, id);
        Assertions.assertThat(responseGetFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFaculty.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(responseGetFaculty.getBody().getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void whenGetByIdFaculty_thenStatusNotFound() {
        final long id = 1000;
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/{id}", String.class, id);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetByColorFaculty_thenStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("biology");
        faculty.setColor("blue");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:"
                + port + "/faculty", faculty, Faculty.class);
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/color?color=" + faculty.getColor(), String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenGetByColorFaculty_thenStatusNotFound() {
        String color = "22222";
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/color?color=" + color, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetByColorOrNameFaculty_thenStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("writer");
        faculty.setColor("violet");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:"
                + port + "/faculty", faculty, Faculty.class);
        String color = (String) responseAddFaculty.getBody().getColor();
        String name = (String) responseAddFaculty.getBody().getName();

        // указали название факультета
        ResponseEntity<Faculty> responseGetFacultyByName = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?name=" + name, Faculty.class);
        Assertions.assertThat(responseGetFacultyByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFacultyByName.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(responseGetFacultyByName.getBody().getColor()).isEqualTo(faculty.getColor());

        // указали цвет факультета
        ResponseEntity<Faculty> responseGetFacultyByColor = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?color=" + color, Faculty.class);
        Assertions.assertThat(responseGetFacultyByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFacultyByColor.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(responseGetFacultyByColor.getBody().getColor()).isEqualTo(faculty.getColor());

        // указали название и цвет факультета
        ResponseEntity<Faculty> responseGetFacultyByNameAndColor = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?name=" + name + "&color=" + color, Faculty.class);
        Assertions.assertThat(responseGetFacultyByNameAndColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFacultyByNameAndColor.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(responseGetFacultyByNameAndColor.getBody().getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void whenGetByColorOrNameFaculty_thenStatusNotFound() {
        String color = "rfjenrjbhber";
        String name = "rgrgkrrtm";

        // указали название факультета
        ResponseEntity<String> responseGetFacultyByName = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?name=" + name, String.class);
        Assertions.assertThat(responseGetFacultyByName.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // указали цвет факультета
        ResponseEntity<String> responseGetFacultyByColor = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?color=" + color, String.class);
        Assertions.assertThat(responseGetFacultyByColor.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // указали цвет и название факультета
        ResponseEntity<String> responseGetFacultyByNameAndColor = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?name=" + name + "&color=" + color, String.class);
        Assertions.assertThat(responseGetFacultyByNameAndColor.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // не указали название и цвет факультета
        ResponseEntity<String> responseGetFacultyByNameAndColor1 = restTemplate.getForEntity("http://localhost:"
                + port + "/faculty/colorOrName?name=&color=", String.class);
        Assertions.assertThat(responseGetFacultyByNameAndColor1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetAllFaculties() {

        ResponseEntity<List<Faculty>> response = restTemplate.exchange("http://localhost:" + port + "/faculty", GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().size()).isNotNull();
    }

    @Test
    public void whenGetStudentsByFacultyId_thenStatusOk() {
        Faculty faculty1 = facultyController.addFaculty(new Faculty(0L, "Griffindor", "green"));
        Faculty faculty2 = facultyController.addFaculty(new Faculty(0L, "Slytherin", "blue"));
        Faculty faculty3 = facultyController.addFaculty(new Faculty(0L, "Hufflepuff", "red"));
        Student student1 = studentController.addStudent(new Student(0L, "Harry", 15));
        student1.setFaculty(faculty1);
        studentController.changeStudent(student1);
        Student student2 = studentController.addStudent(new Student(0L, "Malfoi", 15));
        student2.setFaculty(faculty2);
        studentController.changeStudent(student2);
        Student student3 = studentController.addStudent(new Student(0L, "Ron", 15));
        student3.setFaculty(faculty1);
        studentController.changeStudent(student3);

        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:"
                        + port + "/faculty/idFaculty-by-students?id=" + faculty1.getId(), GET, null,
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().contains(student1)).isTrue();
    }

    @Test
    public void whenGetStudentsByFacultyId_thenStatusNotFound() {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:"
                        + port + "/faculty/idFaculty-by-students?id=" + 1000, GET, null,
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenPutFaculty_thenStatusOk() {
        Faculty facultyForAdd = new Faculty();
        facultyForAdd.setName("ecwgtewcec");
        facultyForAdd.setColor("yellow");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", facultyForAdd, Faculty.class);
        long id = requireNonNull(responseAddFaculty.getBody()).getId();

        Faculty facultyForChange = new Faculty();
        facultyForChange.setId(id);
        facultyForChange.setName("russian language");
        facultyForChange.setColor("yellow");
        ResponseEntity<Void> responseChangeFaculty = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(facultyForChange), Void.class);
        Assertions.assertThat(responseChangeFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenPutFaculty_thenStatusNotFound() {
        Faculty facultyForChange = new Faculty();
        facultyForChange.setId(1000);
        facultyForChange.setName("literature");
        facultyForChange.setColor("black");
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(facultyForChange), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("literature");
        faculty.setColor("black");

        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        long id = requireNonNull(responseAddFaculty.getBody()).getId();

        ResponseEntity<Faculty> responseFindFaculty = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, Faculty.class);
        Assertions.assertThat(responseFindFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + id);

        ResponseEntity<String> responseCheckFaculty = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, String.class);
        Assertions.assertThat(responseCheckFaculty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
