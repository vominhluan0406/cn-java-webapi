package com.shop.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.shop.entity.Image;
import com.shop.respository.ImageRepository;
import com.shop.service.AmazonClient;

@Service
public class AmazonClientImp implements AmazonClient {

	private AmazonS3 amazonS3;

	@Autowired
	private ImageRepository imageRepository;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.amazonS3 = new AmazonS3Client(credentials);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		amazonS3.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	@Override
	public List<String> uploadFile(MultipartFile[] multipartFile) {

		List<String> fileNames = new ArrayList<String>();
		for (MultipartFile f : multipartFile) {
			try {
				File file = convertMultiPartToFile(f);
				String fileName = generateFileName(f);
				fileNames.add(endpointUrl + "/" + bucketName + "/" + fileName);
				uploadFileTos3bucket(fileName, file);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileNames;
	}

	@Override
	public void deleteFile(long id) {
		Image image = imageRepository.findById(id).get();
		String fileUrl = image.getUrl();

		amazonS3.deleteObject(bucketName, fileUrl);

	}

	@Override
	public void deleteFile(String url) {
		AmazonS3URI fileUrl = new AmazonS3URI(url);
		amazonS3.deleteObject(fileUrl.getBucket(), fileUrl.getKey());

	}
}
