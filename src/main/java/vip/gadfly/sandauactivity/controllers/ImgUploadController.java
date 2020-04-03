package vip.gadfly.sandauactivity.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ImgUploadController {
    private final RestTemplate restTemplate;

    public ImgUploadController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "/upload/img", consumes= { MediaType.MULTIPART_FORM_DATA_VALUE})
    public GlobalJSONResult uploadHandler(@RequestParam("file") MultipartFile reqData) throws JsonProcessingException {
        String imgData = uploadViaSmms(reqData);
        //调用jackson
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode imgMapData = objectMapper.readTree(imgData);
        HashMap<String, String> resultData = new HashMap<String, String>();
        String code = imgMapData.path("code").asText();
        if (!code.equals("success")) {
            if (code.equals("image_repeated")) {
                resultData.put("url", imgMapData.path("images").asText());
                return GlobalJSONResult.ok(resultData, "上传成功");
            }
            return GlobalJSONResult.errorMsg("上传失败，请检查");
        }

        resultData.put("url", imgMapData.path("data").get("url").asText());
        return GlobalJSONResult.ok(resultData, "上传成功！");
    }

    private String uploadViaSmms(MultipartFile file) {

        // 支持中文
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        String requestUrl = "https://sm.ms/api/v2/upload";

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("smfile", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("mxIvkuFX2BuOheQDs9fpsvF3rx9SOAe4");
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, requestEntity, String.class);

        return response.getBody();
    }
}
