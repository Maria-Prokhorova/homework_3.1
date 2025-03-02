package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

import static java.util.Objects.requireNonNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

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
        Assertions.assertThat(response.getBody().getName()).isEqualTo("math");
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("green");
    }

    @Test
    public void whenGetByIdFaculty_thenStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("law");
        faculty.setColor("red");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(responseAddFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        int id = (int) responseAddFaculty.getBody().getId();

        ResponseEntity<Faculty> responseGetFaculty = restTemplate.getForEntity("http://localhost:" + port + "/faculty/{id}", Faculty.class, id);
        Assertions.assertThat(responseGetFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFaculty.getBody().getName()).isEqualTo("law");
        Assertions.assertThat(responseGetFaculty.getBody().getColor()).isEqualTo("red");
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
        faculty.setName("law1");
        faculty.setColor("red1");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/color?color=" + faculty.getColor(), String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenGetByColorFaculty_thenStatusNotFound() {
        String color = "22222";
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/color?color=" + color, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenGetByColorOrNameFaculty_thenStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("111111");
        faculty.setColor("111111");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        String color = (String) responseAddFaculty.getBody().getColor();
        String name = (String) responseAddFaculty.getBody().getName();

        ResponseEntity<Faculty> responseGetFacultyByColor = restTemplate.getForEntity("http://localhost:" + port + "/faculty/colorOrName?color=" + color, Faculty.class);
        Assertions.assertThat(responseGetFacultyByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFacultyByColor.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(responseGetFacultyByColor.getBody().getColor()).isEqualTo(faculty.getColor());

        ResponseEntity<Faculty> responseGetFacultyByName = restTemplate.getForEntity("http://localhost:" + port + "/faculty/colorOrName?name=" + name, Faculty.class);
        Assertions.assertThat(responseGetFacultyByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetFacultyByName.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(responseGetFacultyByName.getBody().getColor()).isEqualTo(faculty.getColor());
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
    public void testGetFaculty() {
       Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isNotEmpty();
    }

    @Test
    public void testGetStudentsByFacultyId() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/idFaculty-by-students", String.class))
                .isNotEmpty();
    }

    @Test
    public void whenPutFaculty_thenStatusOk() {
        Faculty facultyForAdd = new Faculty();
        facultyForAdd.setName("math");
        facultyForAdd.setColor("red");
        ResponseEntity<Faculty> responseAddFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", facultyForAdd, Faculty.class);
        int id = (int) responseAddFaculty.getBody().getId();

        Faculty facultyForChange = new Faculty();
        facultyForChange.setId(id);
        facultyForChange.setName("rush");
        facultyForChange.setColor("black");
        ResponseEntity response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(facultyForChange), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void whenPutFaculty_thenStatusNotFound() {
        Faculty facultyForChange = new Faculty();
        facultyForChange.setId(1000);
        facultyForChange.setName("rush");
        facultyForChange.setColor("black");
        ResponseEntity response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(facultyForChange), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long savedFacultyId = requireNonNull(facultyResponseEntity.getBody()).getId();

        ResponseEntity<Faculty> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + savedFacultyId, Faculty.class);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + savedFacultyId);

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + savedFacultyId, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
