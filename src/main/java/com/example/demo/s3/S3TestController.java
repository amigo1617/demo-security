package com.example.demo.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class S3TestController {

    @Autowired
    private S3FileHandler s3FileHandler;

    @PostMapping("/api/upload")
    public ResponseEntity updateUserImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String url = s3FileHandler.uploadFile(multipartFile, "temp");
        return new ResponseEntity(url, HttpStatus.OK);
    }
}
/*
다운로드 예시

    @RequestMapping(value = "/api/cmm/file/download", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<InputStreamResource> download(
            @RequestBody @Valid CmmInDTO<FileInVO> inDTO) throws Exception {
        FileInVO param = inDTO.getBody();
        // 파라미터 검증
        if (UcmStringUtil.isEmpty(param.getFileNo())) {
            throw new UcmException(UcmMessage.E9008);
        }
        if (param.getSn() == null) {
            throw new UcmException(UcmMessage.E9008);
        }

        FileOutVO resultVO = fileService.selectFile(param);

        if (resultVO == null) {
            throw new UcmException(UcmMessage.E9007);
        }
        if (UcmStringUtil.isEmpty(resultVO.getAtchFileActlNm())) {
            throw new UcmException(UcmMessage.E9007);
        }
        if (UcmStringUtil.isEmpty(resultVO.getAtchFileNm())) {
            throw new UcmException(UcmMessage.E9007);
        }
        if (UcmStringUtil.isEmpty(resultVO.getAtchFileUrlAddr())) {
            throw new UcmException(UcmMessage.E9007);
        }

        // 파일 다운로드
        var downloadedFile = amazonS3.getObject(bucket, resultVO.getAtchFileUrlAddr() + "/" + resultVO.getAtchFileNm());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(downloadedFile.getObjectMetadata().getContentLength());
        headers.add("Content-Disposition", UcmHttpUtil.getContentDisposition(resultVO.getAtchFileActlNm()));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new InputStreamResource(downloadedFile.getObjectContent()));
    }

    */
