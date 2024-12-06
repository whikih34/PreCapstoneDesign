package com.springboot.apiserver.ppurio.service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.apiserver.ppurio.config.PpurioConfig;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioFileDto;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioMMSRequestDto;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioSMSRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PpurioService {
    private final PpurioConfig ppurioConfig;
    private final RestTemplate restTemplate= new RestTemplate();
    public String requestToken(){
        String token = null;
        String authString = ppurioConfig.getPpurioid() + ":" + ppurioConfig.getPpruioApikey();
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

        String authorizationHeader = "Basic " + encodedAuthString;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
//        System.out.println(ppurioConfig.getPpurioUrl()+"/v1/token");
//        System.out.println(headers);
        String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl()+"/v1/token", entity, String.class);
//        System.out.println(response);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            token = rootNode.get("token").asText();
            ppurioConfig.setToken(token);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public String sendMessage(PpurioSMSRequestDto ppurioSMSRequestDto) throws IOException {
//        만약 기존 발급된 토큰이 있었다면?? 관련 로직 필요
        if(ppurioConfig.getToken()==null){
            requestToken();
        }
        String bearerAuthorization = String.format("%s %s", "Bearer", ppurioConfig.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerAuthorization);
//        Map<String, Object> sendParams = createSendTestParams();

        ppurioSMSRequestDto.setAccount(ppurioConfig.getPpurioid());
        ppurioSMSRequestDto.setFrom(ppurioConfig.getPpruioFrom());
        ppurioSMSRequestDto.setMessageType("SMS");
        Map<String,Object> sendParams = ppurioSMSRequestDto.toMap();
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(sendParams,headers);
//        System.out.println(sendParams.toString());
//        sendParams.put("files", List.of(null));
        String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl()+"/v1/message", entity, String.class);
        System.out.println("SMS RESPONSE : "+response);
        return response;
    }


    public String sendMessage(PpurioMMSRequestDto ppurioMMSRequestDto)throws IOException{
        if(ppurioConfig.getToken()==null){
            requestToken();
        }
        String bearerAuthorization = String.format("%s %s", "Bearer", ppurioConfig.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerAuthorization);
//        Map<String, Object> sendParams = createSendTestParams();
        System.out.println("atservice : "+ppurioMMSRequestDto.getImgUrl());
        ppurioMMSRequestDto.setAccount(ppurioConfig.getPpurioid());
        ppurioMMSRequestDto.setFrom(ppurioConfig.getPpruioFrom());
        ppurioMMSRequestDto.setSubject("Team.ManToMan");
        ppurioMMSRequestDto.setMessageType("MMS");
        PpurioFileDto base64File = encodeFileFromUrl(ppurioMMSRequestDto.getImgUrl());
        ppurioMMSRequestDto.setFiles(List.of(base64File)); // 파일 리스트 설정
        System.out.println(ppurioMMSRequestDto.toString());
        Map<String,Object> sendParams = ppurioMMSRequestDto.toMap();
        System.out.println(ppurioMMSRequestDto.toString());

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(sendParams,headers);

        try {
            String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl() + "/v1/message", entity, String.class);
            System.out.println("response : " + response);
            return response;
        } catch (Exception e) {
            System.err.println("Error during REST call: " + e.getMessage());
            e.printStackTrace();
            return null; // 또는 적절한 오류 메시지 반환
        }
    }

    public PpurioFileDto encodeFileFromUrl(String imgUrl) throws IOException {
        System.out.println("encoding fun: "+imgUrl);
        if (imgUrl == null || imgUrl.isEmpty()) {
            System.out.println("Image URL is empty or null.");
            return null;
        }

        try (InputStream inputStream = new URL(imgUrl).openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // URL로부터 파일 다운로드
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 다운로드된 데이터
            byte[] fileBytes = outputStream.toByteArray();

            // Base64로 인코딩
            String base64EncodedData = Base64.getEncoder().encodeToString(fileBytes);

            String originalFileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1); // 전체 파일 이름
            String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.')); // 확장자 제거
            String fileName = fileNameWithoutExtension + ".jpg"; // 확장자 강제로 변경


            // PpurioFileDto 객체 생성
            PpurioFileDto base64File = new PpurioFileDto();
            base64File.setName(fileName);
            base64File.setSize(fileBytes.length);
            base64File.setData(base64EncodedData);

            System.out.println("Encoded Base64 " + fileName +", size : "+fileBytes.length);
            return base64File;
        } catch (IOException e) {
            System.err.println("Error processing URL: " + imgUrl);
            e.printStackTrace();
            throw e;
        }
    }



}
