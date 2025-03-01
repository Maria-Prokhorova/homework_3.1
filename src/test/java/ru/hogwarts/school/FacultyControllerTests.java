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
    public void testPostFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("math");
        faculty.setColor("green");
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotEmpty();
    }

    @Test
    public void testGetByIdFaculty() {
        final long id = 1;
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/{id}", String.class, id))
                .isNotNull();
    }

    @Test
    public void testGetByColorFaculty() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/color", String.class, "red"))
                .isNotNull();
    }

    @Test
    public void testGetByColorOrNameFaculty() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/colorOrName", String.class, "math"))
                 .isNotEmpty();
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
    public void testPutFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setName("math");
        faculty.setColor("red");

        ResponseEntity response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, new HttpEntity<>(faculty), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long savedFacultyId = requireNonNull(facultyResponseEntity.getBody()).getId();

        ResponseEntity<Faculty> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + savedFacultyId, Faculty.class);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + savedFacultyId);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + savedFacultyId, Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
