package com.springboot.apiserver.s3.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String upload(String filePath) throws RuntimeException {
        try {
            System.out.println("Original FilePath: " + filePath);
            Path originalPath = Paths.get(filePath);

            // PNG 데이터를 JPG로 변환 및 압축
            Path jpgPath = convertPngToJpg(originalPath, 800, 600, 0.7f);

            // S3 업로드
            String fileName = jpgPath.getFileName().toString();
            String uploadImageUrl = putS3(jpgPath, fileName);

            // 변환된 파일 삭제
            Files.deleteIfExists(jpgPath);

            return uploadImageUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process and upload file", e);
        }
    }

    private Path convertPngToJpg(Path pngPath, int targetWidth, int targetHeight, float quality) throws IOException {
        // PNG 읽기
        BufferedImage pngImage = ImageIO.read(pngPath.toFile());

        // JPG 변환을 위한 리사이징
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(pngImage, 0, 0, targetWidth, targetHeight, Color.WHITE, null);
        graphics.dispose();

        // JPG 파일 생성
        File jpgFile = new File("converted_" + pngPath.getFileName().toString().replace(".png", ".jpg"));
        try (FileOutputStream fos = new FileOutputStream(jpgFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {

            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality); // 압축 품질 설정

            writer.setOutput(ios);
            writer.write(null, new IIOImage(resizedImage, null, null), param);
            writer.dispose();
        }

        return jpgFile.toPath();
    }

    private String putS3(Path filePath, String fileName) throws RuntimeException {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    filePath);
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    //사용자 업로드용 메서드
    public String uploadUserFile(int id, MultipartFile multipartFile) {
        try {
            // MultipartFile에서 파일 이름 생성
            String originalFileName = multipartFile.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IllegalArgumentException("Invalid file name");
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String newFileName = originalFileName.replaceAll(" ", "_").replace(".png", "") + "_" +id+"_"+timestamp + ".jpg";

            // PNG 데이터를 JPG로 변환 및 압축
            Path tempFilePath = Files.createTempFile("upload_", ".png");
            multipartFile.transferTo(tempFilePath.toFile());
            Path jpgPath = convertPngToJpg(tempFilePath, 800, 600, 0.7f);

            // S3에 업로드
            String uploadImageUrl = putS3(jpgPath, newFileName);

            // 임시 파일 삭제
            Files.deleteIfExists(tempFilePath);
            Files.deleteIfExists(jpgPath);

            return uploadImageUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process and upload file", e);
        }
    }

}
