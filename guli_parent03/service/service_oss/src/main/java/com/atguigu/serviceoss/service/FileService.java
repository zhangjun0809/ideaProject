package com.atguigu.serviceoss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhangjun
 * @create 2020-02-07 19:24
 */
public interface FileService {
    String uploadFileOss(MultipartFile file);


}
