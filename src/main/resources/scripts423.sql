select student.name, student.age, faculty.name, faculty.color
from student left join faculty on student.faculty_id=faculty.id

select student.name, avatar.file_path
from student right join avatar on avatar.student_id=student.id