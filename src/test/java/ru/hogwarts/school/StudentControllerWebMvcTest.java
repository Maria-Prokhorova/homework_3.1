package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    private Student student;
    private Collection<Student> students;
    private Faculty faculty;

    @BeforeEach
    public void setup() {
        student = new Student(1L, "Maria", 20);

        students = new ArrayList<>(List.of(
                new Student(1L, "name1", 15),
                new Student(2L, "name2", 18),
                new Student(3L, "name3", 20))
        );

        faculty = new Faculty(1L, "name1", "color1");
    }

    @Test
    public void whenAddStudent() throws Exception {
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        ResultActions perform = mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))
                .accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());

        verify(studentService).addStudent(any(Student.class));
    }

    @Test
    public void whenGetStudentById_thenStatusOk() throws Exception {
        when(studentService.getStudent(any(Long.class))).thenReturn(student);

        ResultActions perform = mockMvc.perform(get("/student/" + student.getId())
                .accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());

        verify(studentService).getStudent(any(Long.class));
    }

    @Test
    public void whenGetStudentsByAge_thenStatusOk() throws Exception {
        final int age = 20;
        when(studentService.getStudentsByAge(any(Integer.class))).thenReturn((List<Student>) students);

        ResultActions perform = mockMvc.perform(get("/student/age?age=" + age).accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andDo(print());

        verify(studentService).getStudentsByAge(any(Integer.class));
    }

    @Test
    public void whenGetAllStudents_thenStatusOk() throws Exception {
        when(studentService.getAllStudents()).thenReturn((List<Student>) students);

        ResultActions perform = mockMvc.perform(get("/student").accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andDo(print());

        verify(studentService).getAllStudents();
    }

    @Test
    public void whenGetStudentByAge_thenStatusOk() throws Exception {
        when(studentService.getStudentsByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn((List<Student>) students);

        ResultActions perform = mockMvc.perform(get("/student/age-between?min=15&max=30").accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andDo(print());

        verify(studentService).getStudentsByAgeBetween(any(Integer.class), any(Integer.class));
    }

    @Test
    public void whenGetQuantityStudents() throws Exception {
        when(studentService.qetQuantityStudents()).thenReturn(20);

        ResultActions perform = mockMvc.perform(get("/student/quantity-students").accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(20))
                .andDo(print());

        verify(studentService).qetQuantityStudents();
    }

    @Test
    public void whenGetAverageAgeStudent() throws Exception {
        when(studentService.getAverageAgeStudent()).thenReturn(20);

        ResultActions perform = mockMvc.perform(get("/student/average-age").accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(20))
                .andDo(print());

        verify(studentService).getAverageAgeStudent();
    }

    @Test
    public void whenGetLastStudents() throws Exception {
        when(studentService.getLastStudents()).thenReturn((List<Student>) students);

        ResultActions perform = mockMvc.perform(get("/student/last-students").accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andDo(print());

        verify(studentService).getLastStudents();
    }

    @Test
    public void whenGetStudentsByFacultyId_thenStatus() throws Exception {
        Student mockStudent = mock(Student.class);
        when(mockStudent.getFaculty()).thenReturn(faculty);
        when(studentService.getStudent(any(Long.class))).thenReturn(mockStudent);

        ResultActions perform = mockMvc.perform(get("/student/idStudent-by-faculty?id=1").accept(MediaType.APPLICATION_JSON));
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void whenUpdateStudent_thenStatusOk() throws Exception {
        when(studentService.changeStudent(any(Student.class))).thenReturn(student);

        ResultActions perform = mockMvc.perform(put("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))
                .accept(MediaType.APPLICATION_JSON)
        );
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());

        verify(studentService).changeStudent(any(Student.class));
    }

    @Test
    public void whenDeleteFaculty() throws Exception {
        long id = 1L;
        ResultActions perform = mockMvc.perform(delete("/student/" + id));
        perform
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(studentService).deleteStudent(any(Long.class));
    }
}
