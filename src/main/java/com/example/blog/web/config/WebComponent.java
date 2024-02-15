package com.example.blog.web.config;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class WebComponent {
    public String ckUpload(MultipartHttpServletRequest request) {

        // ckeditor 에서 파일을 보낼 때 upload : [파일] 형식으로 해서 넘어오기 때문에 upload라는 키의 밸류를 받아서 uploadFile에 저장함
        MultipartFile uploadFile = request.getFile("upload");

        String fileName = getFileName(uploadFile);

        String realPath = getPath(request);

        // 저장 경로
        String savePath = realPath + fileName;

        // 브라우저에서 이미지 불러올 때 절대 경로로 불러오면 보안의 위험 있어 상대경로를 쓰거나 이미지 불러오는 jsp 또는 클래스 파일을 만들어 가져오는 식으로 우회해야 함
        // 때문에 savePath와 별개로 상대 경로인 uploadPath 만들어줌 (controller와 path가 같아야 함)
        String uploadPath = "http://localhost:8080/api/boards/image/upload/" + fileName;
//        String uploadPath = request.getContextPath() + "/boards/image/upload/" + fileName;


        uploadFile(savePath, uploadFile);

        return uploadPath;
    }

    private void uploadFile(String savePath, MultipartFile uploadFile) {
        // 저장 경로로 파일 객체 생성
        File file = new File(savePath);
        try {
            // 파일 업로드
            uploadFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload the file", e);
        }
    }

    // 서버에 저장될 때 중복된 파일 이름인 경우를 방지하기 위해 UUID에 확장자를 붙여 새로운 파일 이름을 생성
    private String getFileName(MultipartFile uploadFile) {
        // 파일의 오리지널 네임
        String originalFileName = uploadFile.getOriginalFilename();
        // 파일의 확장자
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

    private String getPath(MultipartHttpServletRequest request) {
        // 실제 파일 저장 경로
        // 이미지를 현재 경로와 연관된 파일에 저장하기 위해 현재 경로를 알아냄 (controller와 path가 같아야 함)
        String realPath = request.getServletContext().getRealPath("/boards/image/upload/");
        Path directoryPath = Paths.get(realPath);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
        return realPath;
    }

}
