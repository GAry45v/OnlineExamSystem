package cn.edu.zjut.service;

import cn.edu.zjut.controller.QuestionBankController;
import cn.edu.zjut.DTO.PaperQuestionDTO;
import cn.edu.zjut.entity.Papers;
import cn.edu.zjut.entity.Questions;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface PaperService {
    void createPaper(Papers paper);


    Papers autoGeneratePaper(int paperId, String questionBankId, Map<String, Integer> questionTypeCount, Map<String, Integer> questionTypeScore, Integer targetDifficulty);



    void importQuestionsToPaper(int paperId, List<Questions> questions, List<Integer> questionScores);


    void addQuestionToPaper(int paperId, Questions question, int marks);

    void addQuestionManually(int paperId, Questions question, List<MultipartFile> files, List<QuestionBankController.FileMetadata> fileMetadataList, int marks) throws Exception;

    void deleteQuestionFromPaper(int paperQuestionId);
    List<Papers> getPapersByTeacher(String employeeNumber);

    List<PaperQuestionDTO> getQuestionsWithDetailsByPaperId(int paperId);

    boolean deletePaperById(int paperId);
}
