package bd.edu.seu.library_management_system.service;

import bd.edu.seu.library_management_system.model.Teacher;
import bd.edu.seu.library_management_system.repository.TeacherRepository;
import org.springframework.stereotype.Service;


@Service
public class TeacherService {
   private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

}
