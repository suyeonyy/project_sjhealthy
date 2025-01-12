package com.example.sjhealthy.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Controller
public class MainController {

    // board 컨트롤러는 /sjhealthy/로 맵핑 되어서 뷰에서 src로 연결할 때 오류나고 새로 컨트롤러 만들기는 번거로워서 일단 여기로
    @ResponseBody
    @GetMapping("/uploads/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws UnsupportedEncodingException, MalformedURLException {
        // 파일 경로
//        fileName = URLEncoder.encode(fileName, "UTF-8");
        File file = new File("uploads/files/" + fileName);
        System.out.println("filePath = " + file);

        if (!file.exists()){
            System.out.println("첨부파일 없음");
            return ResponseEntity.notFound().build();
        }
        // 파일 URL 생성
//        String fileUrl = "http://localhost:8081/sjhealthy/uploads/files/" + fileName;
        System.out.println("첨부파일 존재");
        // 파일을 resource로 감싸서 반환, URL만으로 이미지를 표시 가능
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(resource);
    }
    @RequestMapping("/sjhealthy")
    public String index(@SessionAttribute(name = "loginId", required = false) String loginId, Model model) {
        System.out.println("index");
        model.addAttribute("loginId", loginId);

        return "main";
    }
}
