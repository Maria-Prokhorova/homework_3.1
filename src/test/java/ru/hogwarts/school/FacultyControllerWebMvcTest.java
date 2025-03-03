package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    private Faculty faculty;
    private List<Faculty> faculties;
    private List<Student> students;

    @BeforeEach
    public void setup() {
        faculty = new Faculty(1L, "history", "red");

        faculties = new ArrayList<>(List.of(
                new Faculty(1L, "name1", "color1"),
                new Faculty(2L, "name2", "color2"),
                new Faculty(3L, "name3", "color3"))
        );

        students = new ArrayList<>(List.of(
                new Student(1L, "name1", 18),
                new Student(2L, "name2", 20),
                new Student(3L, "name3", 25))
        );
    }

    @Test
    public void whenAddFaculty() throws Exception {
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());

        verify(facultyService).addFaculty(any(Faculty.class));
    }

    @Test
    public void whenGetFacultyById_thenStatusOk() throws Exception {
        when(facultyService.findFaculty(any(Long.class))).thenReturn((faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

        verify(facultyService).findFaculty(any(Long.class));
    }

    @Test
    public void whenGetFacultyByColor_thenStatusOk() throws Exception {
        final String color = "white";

        when(facultyService.getFacultiesByColor(any(String.class))).thenReturn((List<Faculty>) faculties);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color?color=" + color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(faculties.size()))
                .andDo(print());

        verify(facultyService).getFacultiesByColor(any(String.class));
    }

    @Test
    public void whenGetFacultyByColorOrName_thenStatusOk() throws Exception {
        when(facultyService.findFacultyByColorOrName(any(String.class), any(String.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/colorOrName?color=" + faculty.getColor() + "&name=")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/colorOrName?name=" + faculty.getName() + "&color=")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/colorOrName?color=" + faculty.getColor() + "&name=" + faculty.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void whenGetAllFaculties_thenStatusOk() throws Exception {
        when(facultyService.getAllFaculties()).thenReturn((List<Faculty>) faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(faculties.size()))
                .andDo(print());

        verify(facultyService).getAllFaculties();
    }

    @Test
    public void whenGetStudentsByFacultyId_thenStatus() throws Exception {
        Faculty mockFaculty = mock(Faculty.class);
        when(mockFaculty.getStudents()).thenReturn(students);
        when(facultyService.findFaculty(any(Long.class))).thenReturn(mockFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/idFaculty-by-students?id=" + 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andDo(print());
    }

    @Test
    public void whenUpdateFaculty_thenStatusOk() throws Exception {
        when(facultyService.changeFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());

        verify(facultyService).changeFaculty(any(Faculty.class));
    }

    @Test
    public void whenDeleteFaculty() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id))
                .andExpect(status().isOk())
                .andDo(print());

        verify(facultyService).deleteFaculty(any(Long.class));
    }
}
