package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testPostStudent() {
        Student student = new Student();
        student.setName("math");
        student.setAge(29);
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotEmpty();
    }

    @Test
    public void testGetByIdStudent() {
        final long id = 1;
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/{id}", String.class, id))
                .isNotNull();
    }

  /*  @Test
    void testCreateStudent() {
        Student student = new Student(1L, "Harry", 15);
        String url = BASE_URL + port + "/student";

        ResponseEntity<String> response = this.restTemplate.postForEntity(url, student, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();*/

}
