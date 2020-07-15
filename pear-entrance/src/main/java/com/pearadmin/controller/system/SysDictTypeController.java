package com.pearadmin.controller.system;

import com.github.pagehelper.PageInfo;
import com.pearadmin.common.constant.MessageConstants;
import com.pearadmin.common.web.base.BaseController;
import com.pearadmin.common.web.domain.request.PageDomain;
import com.pearadmin.common.web.domain.response.Result;
import com.pearadmin.common.web.domain.response.ResultTable;
import com.pearadmin.resource.sequence.pool.SequencePool;
import com.pearadmin.system.domain.SysDictType;
import com.pearadmin.system.service.ISysDictTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Describe: 数据字典控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@RestController
@RequestMapping("system/dictType")
public class SysDictTypeController extends BaseController {

    @Resource
    private ISysDictTypeService sysDictTypeService;

    @Resource
    private SequencePool sequencePool;

    private String MODULE_PATH = "system/dictType/";

    /**
     * Describe: 数据字典列表视图
     * Param: ModelAndView
     * Return: ModelAndView
     * */
    @GetMapping("main")
    public ModelAndView main(){
        return JumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 数据字典列表数据
     * Param: sysDictType
     * Return: ResuTable
     * */
    @GetMapping("data")
    public ResultTable data(SysDictType sysDictType, PageDomain pageDomain){
        PageInfo<SysDictType> pageInfo = sysDictTypeService.page(sysDictType,pageDomain);
        return pageTable(pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * Describe: 数据字典类型新增视图
     * Param: sysDictType
     * Return: ModelAndView
     * */
    @GetMapping("add")
    public ModelAndView add(){
        return JumpPage(MODULE_PATH + "add");
    }

    /**
     * Describe: 新增字典类型接口
     * Param: sysDictType
     * Return: ResuBean
     * */
    @PostMapping("save")
    public Result save(@RequestBody SysDictType sysDictType){
        sysDictType.setId(sequencePool.getStringId());
        boolean result = sysDictTypeService.save(sysDictType);
        return decide(
                result,                           // 响应结果
                MessageConstants.SAVE_SUCCESS,     // 成功消息
                MessageConstants.SAVE_FAILURE      // 失败消息
        );
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: sysDictType
     * Return: ModelAndView
     * */
    @GetMapping("edit")
    public ModelAndView edit(Model model, String dictTypeId){
        model.addAttribute("sysDictType",sysDictTypeService.getById(dictTypeId));
        return JumpPage(MODULE_PATH + "edit");
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: sysDictType
     * Return: ModelAndView
     * */
    @PutMapping("update")
    public Result update(@RequestBody SysDictType sysDictType){
        boolean result =  sysDictTypeService.updateById(sysDictType);
        return decide(result);
    }

    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id")String id){
        Boolean result = sysDictTypeService.remove(id);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 启用字典
     * Param dictId
     * Return ResuTree
     * */
    @PutMapping("enable")
    public Result enable(@RequestBody SysDictType sysDictType){
        sysDictType.setEnable("0");
        boolean result = sysDictTypeService.updateById(sysDictType);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 禁用字典
     * Param dictId
     * Return ResuTree
     * */
    @PutMapping("disable")
    public Result disable(@RequestBody SysDictType sysDIctType){
        sysDIctType.setEnable("1");
        boolean result = sysDictTypeService.updateById(sysDIctType);
        return decide(
                result,                              // 响应结果
                MessageConstants.UPDATE_SUCCESS,     // 成功消息
                MessageConstants.UPDATE_FAILURE      // 失败消息
        );
    }


}
