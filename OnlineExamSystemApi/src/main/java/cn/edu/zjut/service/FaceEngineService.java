package cn.edu.zjut.service;

import cn.edu.zjut.face_detect.dto.FaceUserInfo;
import cn.edu.zjut.face_detect.dto.ProcessInfo;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface FaceEngineService {

    List<FaceInfo> detectFaces(ImageInfo imageInfo);

    List<ProcessInfo> process(ImageInfo imageInfo);

    /**
     * 人脸特征
     * @param imageInfo
     * @return
     */
    byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException;

    /**
     * 人脸比对
     * @param faceId
     * @param faceFeature
     * @return
     */
    List<FaceUserInfo> compareFaceFeature(byte[] faceFeature, String faceId) throws InterruptedException, ExecutionException;

    public boolean compareFace(byte[] inputBytes, String targetface);

}
