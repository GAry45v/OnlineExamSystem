package com.rabbiter.oes.serviceimpl;

import com.rabbiter.oes.entity.Admin;
import com.rabbiter.oes.entity.Teacher;
import com.rabbiter.oes.mapper.AdminMapper;
import com.rabbiter.oes.mapper.TeacherMapper;
import com.rabbiter.oes.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    @Override
    public Admin findById(Integer adminId) {
        return adminMapper.findById(adminId);
    }

    @Override
    public int deleteById(int adminId) {
        return adminMapper.deleteById(adminId);
    }

    @Override
    public int update(Admin admin) {
        return adminMapper.update(admin);
    }

    @Override
    public int add(Admin admin) {
        return 0;
    }

    @Override
    public Object resetPsw(Integer adminId, String newPsw, String oldPsw) {
        Admin admin = findById(adminId);
        if(admin != null && admin.getPwd().equals(oldPsw)) {
            admin.setPwd(String.valueOf(newPsw));
            update(admin);
            return true;

        }else if(admin == null){
            Teacher teacher = teacherMapper.findById(adminId);
            if(teacher != null && teacher.getPwd().equals(oldPsw)) {
                teacher.setPwd(String.valueOf(newPsw));
                teacherMapper.update(teacher);
                return true;

            }
        }
        return "原密码错误";
    }


}
