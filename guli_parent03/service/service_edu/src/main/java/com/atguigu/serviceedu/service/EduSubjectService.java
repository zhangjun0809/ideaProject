package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.OnesubjuecVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-08
 */
@Service
public interface EduSubjectService extends IService<EduSubject> {

    List<String> importSubjectData(MultipartFile file);

    List<OnesubjuecVo> getAllSubject();
}

