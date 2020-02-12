package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.OnesubjuecVo;
import com.atguigu.serviceedu.entity.vo.TwoSubjectVo;
import com.atguigu.serviceedu.mapper.EduSubjectMapper;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-08
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {



    @Override
    public List<String> importSubjectData(MultipartFile file) {
        //1获取输入流
        try {
            InputStream in = file.getInputStream();
            //2创建workbook
            //解决excel版本是03 还是07问题  交给工厂创建
            //Workbook workbook1 = WorkbookFactory.create(in);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            //3获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);
            //4获取row
            //4.1获取最后一行行号
            int lastRowNum = sheet.getLastRowNum();
            //创建list封装错误数据
            List<String> msg = new ArrayList<>();
            for (int i = 1; i <=lastRowNum; i++) {
                Row row = sheet.getRow(i);
                //5获取cell
                //获取第一列
                Cell cell = row.getCell(0);
                if (cell==null){
                    String error="第"+(i+1)+"行，第1列数据为空";
                    msg.add(error);
                    continue;
                }
                //获取第二列
                //获取值
                String cellValue = cell.getStringCellValue();
                if(StringUtils.isEmpty(cellValue)){
                    String error="第"+(i+1)+"行，第1列数据为空";
                    msg.add(error);
                    continue;
                }
                EduSubject existeduSubject = this.exisOneSubject(cellValue);
                if (existeduSubject==null){
                    existeduSubject = new EduSubject();
                    existeduSubject.setTitle(cellValue);
                    existeduSubject.setParentId("0");
                    baseMapper.insert(existeduSubject);
                }
                //获取一级分类ID  二级分类要用
                String parentId = existeduSubject.getId();
                //获取值
                //获取第二列
                Cell cell1 = row.getCell(1);
                if (cell1==null){
                    String error="第"+(i+1)+"行，第2列数据为空";
                    msg.add(error);
                    continue;
                }
                //获取值
                String cellValue1 = cell1.getStringCellValue();

                if(StringUtils.isEmpty(cellValue1)){
                    String error="第"+(i+1)+"行，第2列数据为空";
                    msg.add(error);
                    continue;
                }
                EduSubject eduSubject = this.exisTwoSubject(cellValue1, parentId);
                if (eduSubject==null){
                    eduSubject = new EduSubject();
                    eduSubject.setTitle(cellValue1);
                    eduSubject.setParentId(parentId);
                    baseMapper.insert(eduSubject);
                }
            }

            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20002,"添加课程分类失败");//不是全部失败 部分成功
        }
    }

    //Map方法实现
    @Override
    public List<OnesubjuecVo> getAllSubject() {
        //1获取所有一级分类
        //parent_id = 0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //2获取所有一。二级分类
        List<EduSubject> AllSubjectList = baseMapper.selectList(null);

        //最终返回的list集合
        List<OnesubjuecVo> finalSubjectList = new ArrayList<>();
        Map<String,OnesubjuecVo> map = new HashMap<>();

        //3封装一级分类
        for (int i = 0; i <oneSubjectList.size() ; i++) {
            EduSubject oneSubject = oneSubjectList.get(i);
            OnesubjuecVo oneSubjectVo = new OnesubjuecVo();
            BeanUtils.copyProperties(oneSubject, oneSubjectVo);
            finalSubjectList.add(oneSubjectVo);
            map.put(oneSubjectVo.getId(), oneSubjectVo);
        }
       for (EduSubject cc:AllSubjectList){
           if(!cc.getParentId().equals("0")){
               String id = cc.getParentId();
               TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
               BeanUtils.copyProperties(cc, twoSubjectVo);
               OnesubjuecVo onesubjuecVo = map.get(id);
               onesubjuecVo.getChildren().add(twoSubjectVo);
           }
       }
        return finalSubjectList;
    }

    //双层循环实现，低效
//    @Override
//    public List<OnesubjuecVo> getAllSubject() {
//        //1获取所有一级分类
//        //parent_id = 0
//        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
//        wrapperOne.eq("parent_id","0");
//        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
//
//        //2获取所有二级分类
//        //parent_id ！= 0
//        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
//        wrapperTwo.ne("parent_id","0");
//        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
//
//        //最终返回的list集合
//        List<OnesubjuecVo> finalSubjectList = new ArrayList<>();
//        Map<String,OnesubjuecVo> map = new HashMap<>();
//
//        //3封装一级分类
//        for (int i = 0; i <oneSubjectList.size() ; i++) {
//            //获得每一个一级分类
//            EduSubject oneSubject = oneSubjectList.get(i);
//            //oneSubject 转化成OneSubjectVo
//            OnesubjuecVo oneSubjectVo = new OnesubjuecVo();
//            //oneSubjectVo.setId(oneSubject.getId());
//            //oneSubjectVo.setTitle(oneSubject.getTitle());
//            BeanUtils.copyProperties(oneSubject,oneSubjectVo);
//            //oneSubjectVo存入finalSubjectList
//            finalSubjectList.add(oneSubjectVo);
//            map.put(oneSubjectVo.getId(),oneSubjectVo);
//
//            //创建集合，封装二级分类
//            List<TwoSubjectVo> twoVoList = new ArrayList<>();
//            for (int m = 0; m < twoSubjectList.size(); m++) {
//                //获取每个二级分类数据
//                EduSubject twoSubject = twoSubjectList.get(m);
//                //判断：一级分类id 和二级分类parentId比较
//                if(oneSubject.getId().equals(twoSubject.getParentId())){
//                    //twoSubject转化成TwoSubjectVo
//                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
//                    BeanUtils.copyProperties(twoSubject,twoSubjectVo);
//                    //放入二级分类集合中
//                    twoVoList.add(twoSubjectVo);
//                }
//
//            }
//            oneSubjectVo.setChildren(twoVoList);
//        }
//
//        return finalSubjectList;
//    }

    //判断一级分类是否重复，双层循环实现
    private EduSubject exisOneSubject(String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject eduSubject = baseMapper.selectOne(wrapper);
        return eduSubject;
    }

    //判断二级分类是否重复
    private EduSubject exisTwoSubject(String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject eduSubject = baseMapper.selectOne(wrapper);
        return eduSubject;
    }
}
