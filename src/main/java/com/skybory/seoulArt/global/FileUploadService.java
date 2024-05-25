package com.skybory.seoulArt.global;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Service
public class FileUploadService {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    private final AmazonS3 s3client;
    private final String bucketName = "skybory-bucket";

    public FileUploadService(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadFile(MultipartFile file, String fileName, String folderName)  throws IOException {
    	String filePath = folderName + "/" + fileName;
    	logger.info("Uploading file {} to bucket {}", filePath, bucketName);
    	
    	ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        
        s3client.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata));
//        URL s3Url = s3client.getUrl(bucketName, fileName);
        logger.info("File uploaded successfully and accessible at {}", filePath);
        return "/" + filePath;
    }
}