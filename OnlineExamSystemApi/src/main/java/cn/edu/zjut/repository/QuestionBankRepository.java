package cn.edu.zjut.repository;

import cn.edu.zjut.entity.QuestionBank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {
    QuestionBank findByName(String name); // 根据题库名称查询
}
