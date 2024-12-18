package cn.edu.zjut.service;

import cn.edu.zjut.entity.Papers;
import cn.edu.zjut.entity.Questions;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface PaperService {
    void createPaper(Papers paper);

    Papers autoGeneratePaper(int paperId, String questionBankId, Map<String, Integer> questionTypeCount, int targetDifficulty);

    void importQuestionsToPaper(int paperId, List<Questions> questions);


    void addQuestionManually(int paperId, Questions question, List<MultipartFile> files) throws Exception;


    void deleteQuestionFromPaper(int paperQuestionId);
    List<Papers> getPapersByTeacher(String employeeNumber);

    List<Questions> getQuestionsWithDetailsByPaperId(int paperId);

}
