package com.skybory.seoulArt.global;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Service
public class FileUploadService {
    private final AmazonS3 s3client;
    private final String bucketName = "skybory-bucket";

    public FileUploadService(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadFile(MultipartFile file, String fileName)  throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
        
        URL s3Url = s3client.getUrl(bucketName, fileName);
        return s3Url.toString();
    }
}