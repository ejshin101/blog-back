package com.example.blog.web.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.blog.web.config.AWSS3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileUploadService {
    private final AWSS3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //임의의 로컬파일 저장 경로 -> 여기에 업로드 한 파일을 임시로 저장한 후 S3에 올리게 된다.
    private String localLocation = "/home/temp/";

    public String imageUpload(MultipartRequest request) throws IOException {
        //request 인자에서 이미지 파일을 뽑아냄
        MultipartFile file = request.getFile("upload");

        //뽑아낸 이미지 파일에서 이름 및 확장자 추출
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        //이미지 파일 이름 유일성을 위해 uuid 생성
        String uuidFileName = UUID.randomUUID() + ext;

        //서버 환경에 저장할 경로 생성
        String localPath = localLocation + uuidFileName;

        //서버 환경에 이미지 파일을 저장
        File localFile = new File(localPath);
        file.transferTo(localFile);

        //s3에 이미지 올림
        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        localFile.delete();

        return s3Url;

    }
}
