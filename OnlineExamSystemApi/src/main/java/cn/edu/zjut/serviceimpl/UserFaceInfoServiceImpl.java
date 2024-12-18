package cn.edu.zjut.serviceimpl;


import cn.edu.zjut.entity.UserFaceInfo;
import cn.edu.zjut.service.UserFaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.zjut.mapper.MybatisUserFaceInfoMapper;

@Service
public class UserFaceInfoServiceImpl implements UserFaceInfoService {


    @Autowired
    private MybatisUserFaceInfoMapper userFaceInfoMapper;

    @Override
    public void insertSelective(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.insertUserFaceInfo(userFaceInfo);
    }
}
