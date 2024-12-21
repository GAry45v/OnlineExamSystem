package cn.edu.zjut.serviceimpl;
import cn.edu.zjut.DTO.StudentAnswerDTO;
import cn.edu.zjut.entity.StudentAnswerAndGrading;
import cn.edu.zjut.mapper.StudentAnswerMapper;
import cn.edu.zjut.service.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {

    @Autowired
    private StudentAnswerMapper studentAnswerMapper;

    @Override
    public void saveAnswer(StudentAnswerAndGrading answer) {
        studentAnswerMapper.insertAnswer(answer);
    }
    @Override
    public void updateAnswerGrading(StudentAnswerDTO answer) {
        studentAnswerMapper.updateAnswerGrading(answer);
    }
}
