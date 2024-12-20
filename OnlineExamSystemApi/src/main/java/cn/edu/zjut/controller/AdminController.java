package cn.edu.zjut.controller;

import cn.edu.zjut.entity.*;
import cn.edu.zjut.entity.Class;
import cn.edu.zjut.service.AdminService;
import cn.edu.zjut.vo.ResponseResult;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 批量添加教师信息（通过 Excel 文件上传）
     *
     * @param file Excel 文件
     * @return 封装的响应结果
     */
    @PostMapping("/teacher/batchAdd")
    public ResponseResult<?> batchAddTeachers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<Teacher> teacherList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                Teacher teacher = new Teacher();
                teacher.setName(row.getCell(0).getStringCellValue()); // 第 1 列：姓名
                teacher.setEmployeeNumber(row.getCell(1).getStringCellValue()); // 第 2 列：员工编号
                teacher.setCollegeId((int) row.getCell(2).getNumericCellValue()); // 第 3 列：学院 ID
                teacher.setSchoolId((int) row.getCell(3).getNumericCellValue()); // 第 4 列：学校 ID
                teacher.setFaceImageId(row.getCell(4).getStringCellValue()); // 第 5 列：人脸图片 ID

                teacherList.add(teacher);
            }

            // 调用 Service 层的批量插入方法
            adminService.addTeachersBatch(teacherList);

            return ResponseResult.success("批量添加教师成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
    @PostMapping("/teacher/add")
    public ResponseResult<?> addTeacher(Teacher teacher) {
        if (teacher == null || teacher.getName() == null || teacher.getEmployeeNumber() == null) {
            return ResponseResult.error("教师信息不完整，添加失败！");
        }
        try {
            adminService.addTeacher(teacher);
            return ResponseResult.success("教师添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("教师添加失败：" + e.getMessage());
        }
    }
    @PostMapping("/student/add")
    public ResponseResult<?> addStudent(@RequestBody Student student) {
        if (student == null || student.getName() == null || student.getStudentNumber() == null) {
            return ResponseResult.error("学生信息不完整，添加失败！");
        }
        try {
            adminService.addStudent(student);
            return ResponseResult.success("学生添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("学生添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加学生信息（通过 Excel 文件上传）
     *
     * @param file Excel 文件
     * @return 封装的响应结果
     */
    @PostMapping("/student/batchAdd")
    public ResponseResult<?> batchAddStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<Student> studentList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                Student student = new Student();
                student.setName(row.getCell(0).getStringCellValue()); // 第 1 列：姓名
                student.setEnrollmentYear((int) row.getCell(1).getNumericCellValue()); // 第 2 列：入学年份
                student.setStudentNumber(row.getCell(2).getStringCellValue()); // 第 3 列：学号
                student.setFaceImageId(row.getCell(3).getStringCellValue()); // 第 4 列：人脸图片 ID
                student.setSchoolId((int) row.getCell(4).getNumericCellValue()); // 第 5 列：学校 ID
                student.setCollegeId((int) row.getCell(5).getNumericCellValue()); // 第 6 列：学院 ID
                student.setMajorId((int) row.getCell(6).getNumericCellValue()); // 第 7 列：专业 ID
                student.setClassId((int) row.getCell(7).getNumericCellValue()); // 第 8 列：班级 ID

                studentList.add(student);
            }

            // 调用 Service 层的批量插入方法
            adminService.addStudentsBatch(studentList);

            return ResponseResult.success("批量添加学生成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
    @PostMapping("/school/add")
    public ResponseResult<?> addSchool(@RequestBody School school) {
        if (school == null || school.getName() == null || school.getName().isEmpty()) {
            return ResponseResult.error("学校信息不完整，添加失败！");
        }
        try {
            adminService.addSchool(school);
            return ResponseResult.success("学校添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("学校添加失败：" + e.getMessage());
        }
    }
    @PostMapping("/school/batchAdd")
    public ResponseResult<?> batchAddSchools(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<School> schoolList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                School school = new School();
                school.setName(row.getCell(0).getStringCellValue()); // 第 1 列：学校名称
                school.setLocation(row.getCell(1).getStringCellValue()); // 第 2 列：学校位置

                schoolList.add(school);
            }

            // 调用 Service 层的批量插入方法
            adminService.addSchoolsBatch(schoolList);

            return ResponseResult.success("批量添加学校成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
    @PostMapping("/class/add")
    public ResponseResult<?> addClass(@RequestBody Class classEntity) {
        if (classEntity == null || classEntity.getName() == null || classEntity.getName().isEmpty()) {
            return ResponseResult.error("班级信息不完整，添加失败！");
        }
        try {
            adminService.addClass(classEntity);
            return ResponseResult.success("班级添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("班级添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加班级信息（通过 Excel 文件上传）
     *
     * @param file Excel 文件
     * @return 封装的响应结果
     */
    @PostMapping("/class/batchAdd")
    public ResponseResult<?> batchAddClasses(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<Class> classList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                Class classEntity = new Class();
                classEntity.setMajorId((int) row.getCell(0).getNumericCellValue()); // 第 1 列：专业 ID
                classEntity.setName(row.getCell(1).getStringCellValue()); // 第 2 列：班级名称

                classList.add(classEntity);
            }

            // 调用 Service 层的批量插入方法
            adminService.addClassesBatch(classList);

            return ResponseResult.success("批量添加班级成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
    @PostMapping("/college/add")
    public ResponseResult<?> addCollege(@RequestBody College college) {
        if (college == null || college.getName() == null || college.getName().isEmpty()) {
            return ResponseResult.error("学院信息不完整，添加失败！");
        }
        try {
            adminService.addCollege(college);
            return ResponseResult.success("学院添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("学院添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加学院信息（通过 Excel 文件上传）
     *
     * @param file Excel 文件
     * @return 封装的响应结果
     */
    @PostMapping("/college/batchAdd")
    public ResponseResult<?> batchAddColleges(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<College> collegeList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                College college = new College();
                college.setSchoolId((int) row.getCell(0).getNumericCellValue()); // 第 1 列：学校 ID
                college.setName(row.getCell(1).getStringCellValue()); // 第 2 列：学院名称

                collegeList.add(college);
            }

            // 调用 Service 层的批量插入方法
            adminService.addCollegesBatch(collegeList);

            return ResponseResult.success("批量添加学院成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
    @PostMapping("/course/add")
    public ResponseResult<?> addCourse(@RequestBody Course course) {
        if (course == null || course.getCourseName() == null || course.getCourseName().isEmpty()) {
            return ResponseResult.error("课程信息不完整，添加失败！");
        }
        try {
            adminService.addCourse(course);
            return ResponseResult.success("课程添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("课程添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加课程信息（通过 Excel 文件上传）
     *
     * @param file Excel 文件
     * @return 封装的响应结果
     */
    @PostMapping("/course/batchAdd")
    public ResponseResult<?> batchAddCourses(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<Course> courseList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                Course course = new Course();
                course.setCourseName(row.getCell(0).getStringCellValue()); // 第 1 列：课程名称
                course.setSemester(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null); // 第 2 列：学期
                course.setCreatedByEmployeeNumber(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null); // 第 3 列：创建人编号

                courseList.add(course);
            }

            // 调用 Service 层的批量插入方法
            adminService.addCoursesBatch(courseList);

            return ResponseResult.success("批量添加课程成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
    @PostMapping("/major/add")
    public ResponseResult<?> addMajor(@RequestBody Major major) {
        if (major == null || major.getName() == null || major.getName().isEmpty()) {
            return ResponseResult.error("专业信息不完整，添加失败！");
        }
        try {
            adminService.addMajor(major);
            return ResponseResult.success("专业添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("专业添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加专业信息（通过 Excel 文件上传）
     *
     * @param file Excel 文件
     * @return 封装的响应结果
     */
    @PostMapping("/major/batchAdd")
    public ResponseResult<?> batchAddMajors(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空！");
        }

        List<Major> majorList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            // 获取 Excel 的第一个 Sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 遍历每一行（从第二行开始，第一行是标题）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 解析每一行数据
                Major major = new Major();
                major.setCollegeId((int) row.getCell(0).getNumericCellValue()); // 第 1 列：学院 ID
                major.setName(row.getCell(1).getStringCellValue()); // 第 2 列：专业名称

                majorList.add(major);
            }

            // 调用 Service 层的批量插入方法
            adminService.addMajorsBatch(majorList);

            return ResponseResult.success("批量添加专业成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("文件解析或数据保存失败：" + e.getMessage());
        }
    }
}
