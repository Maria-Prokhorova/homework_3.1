package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.List;

public interface AvatarService {

    void uploadAvatar (Long bookId, MultipartFile file) throws IOException;

    Avatar findStudentAvatar (Long studentID);

    List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize);
}
