package cn.edu.zjut.face_detect.factory;


import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.UserFaceInfo;
import cn.edu.zjut.face_detect.dto.FaceSearchResDto;
import cn.edu.zjut.face_detect.dto.FaceUserInfo;
import cn.edu.zjut.face_detect.dto.ProcessInfo;
import cn.edu.zjut.service.FaceEngineService;
import cn.edu.zjut.service.UserFaceInfoService;
import cn.edu.zjut.vo.ResponseResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Controller
public class FaceController {

    public final static Logger logger = LoggerFactory.getLogger(FaceController.class);


    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    UserFaceInfoService userFaceInfoService;

    @RequestMapping(value = "/demo")
    public String demo() {
        return "demo";
    }

    /*
    人脸添加
     */
    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> faceAdd(@RequestParam("file") String file) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String faceId = authentication.getUserNumber();
        String name="";
        try {
            if (file == null) {
                return Results.newFailedResult("file is null");
            }
            if (faceId == null) {
                return Results.newFailedResult("faceId is null");
            }
            if (name == null) {
                return Results.newFailedResult("name is null");
            }

            byte[] decode = Base64.decode(base64Process(file));
            ImageInfo imageInfo = ImageFactory.getRGBData(decode);
            if (imageInfo == null || imageInfo.getImageData() == null || imageInfo.getImageData().length == 0) {
                throw new IllegalArgumentException("Failed to parse image data into ImageInfo");
            }
            if (imageInfo.getWidth() <= 0 || imageInfo.getHeight() <= 0) {
                throw new IllegalArgumentException("Invalid image dimensions: width=" + imageInfo.getWidth() + ", height=" + imageInfo.getHeight());
            }
            System.out.println(imageInfo);
            logger.info("ImageInfo created: width={}, height={}, format={}",
                    imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat());
            //人脸特征获取
            byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
            if (bytes == null) {
                System.out.println("nobytes");
                return Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED);
            }

            UserFaceInfo userFaceInfo = new UserFaceInfo();
            userFaceInfo.setName(name);
            userFaceInfo.setFaceId(faceId);
            userFaceInfo.setFaceFeature(bytes);
            userFaceInfo.setGroupId(userFaceInfo.getGroupId());

            //人脸特征插入到数据库
            userFaceInfoService.insertSelective(userFaceInfo);

            logger.info("faceAdd:" + name);
            return Results.newSuccessResult("");
        } catch (Exception e) {
            logger.error("", e);
        }
        return Results.newFailedResult(ErrorCodeEnum.UNKNOWN);
    }

    /*
    人脸识别
     */
    @RequestMapping(value = "/faceSearch_bygroupId", method = RequestMethod.POST)
    @ResponseBody
    public Result<FaceSearchResDto> faceSearch(String file, String faceId) throws Exception {

        if (faceId == null) {
            return Results.newFailedResult("faceId is null");
        }
        byte[] decode = Base64.decode(base64Process(file));
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);


        //人脸特征获取
        byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
        if (bytes == null) {
            return Results.newFailedResult(ErrorCodeEnum.NO_FACE_DETECTED);
        }
        //人脸比对，获取比对结果
        List<FaceUserInfo> userFaceInfoList = faceEngineService.compareFaceFeature(bytes, faceId);

        if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
            FaceUserInfo faceUserInfo = userFaceInfoList.get(0);
            FaceSearchResDto faceSearchResDto = new FaceSearchResDto();
            BeanUtil.copyProperties(faceUserInfo, faceSearchResDto);
            List<ProcessInfo> processInfoList = faceEngineService.process(imageInfo);
            if (CollectionUtil.isNotEmpty(processInfoList)) {
                //人脸检测
                List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);
                int left = faceInfoList.get(0).getRect().getLeft();
                int top = faceInfoList.get(0).getRect().getTop();
                int width = faceInfoList.get(0).getRect().getRight() - left;
                int height = faceInfoList.get(0).getRect().getBottom() - top;

                Graphics2D graphics2D = bufImage.createGraphics();
                graphics2D.setColor(Color.RED);//红色
                BasicStroke stroke = new BasicStroke(5f);
                graphics2D.setStroke(stroke);
                graphics2D.drawRect(left, top, width, height);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufImage, "jpg", outputStream);
                byte[] bytes1 = outputStream.toByteArray();
                faceSearchResDto.setImage("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes1));
                faceSearchResDto.setAge(processInfoList.get(0).getAge());
                faceSearchResDto.setGender(processInfoList.get(0).getGender().equals(1) ? "女" : "男");

            }

            return Results.newSuccessResult(faceSearchResDto);
        }
        return Results.newFailedResult(ErrorCodeEnum.FACE_DOES_NOT_MATCH);
    }


    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public List<FaceInfo> detectFaces(String image) throws IOException {
        byte[] decode = Base64.decode(image);
        if (decode == null || decode.length == 0) {
            throw new IllegalArgumentException("Invalid Base64 image data");
        }
        InputStream inputStream = new ByteArrayInputStream(decode);
        ImageInfo imageInfo = ImageFactory.getRGBData(inputStream);

        if (inputStream != null) {
            inputStream.close();
        }
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);

        return faceInfoList;
    }

    @RequestMapping(value = "/faceSearch_byfaceId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<String> faceSearch_byfaceId(String file) throws Exception {
        // 获取当前用户的 faceId
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String faceId = authentication.getUserNumber();
        if (faceId == null) {
            // 返回错误结果：faceId 为空
            return ResponseResult.error("faceId is null");
        }
        // 解码 Base64 字符串为字节数组
        byte[] decode;
        try {
            decode = Base64.decode(base64Process(file));
        } catch (Exception e) {
            return ResponseResult.error("Failed to decode Base64 file: " + e.getMessage());
        }
        // 将字节数组转换为 BufferedImage
        BufferedImage bufImage;
        try {
            bufImage = ImageIO.read(new ByteArrayInputStream(decode));
            if (bufImage == null) {
                return ResponseResult.error("Invalid image format");
            }
        } catch (Exception e) {
            return ResponseResult.error("Failed to read image: " + e.getMessage());
        }
        // 转换为 ImageInfo 对象
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);
        // 提取人脸特征
        byte[] faceFeature;
        try {
            faceFeature = faceEngineService.extractFaceFeature(imageInfo);
            if (faceFeature == null) {
                return ResponseResult.error("No face detected in the image");
            }
        } catch (Exception e) {
            return ResponseResult.error("Failed to extract face feature: " + e.getMessage());
        }
        // 比对人脸特征
        boolean isMatched;
        try {
            isMatched = faceEngineService.compareFace(faceFeature, faceId);
        } catch (Exception e) {
            return ResponseResult.error("Failed to compare face: " + e.getMessage());
        }
        // 返回比对结果
        if (isMatched) {
            return ResponseResult.success("Face matched successfully");
        } else {
            return ResponseResult.error("Face does not match");
        }
    }


    private String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }

            return base64Str;
        } else {
            return "";
        }
    }
}
