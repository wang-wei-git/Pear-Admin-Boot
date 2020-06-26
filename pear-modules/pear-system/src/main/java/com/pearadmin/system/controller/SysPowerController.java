package com.pearadmin.system.controller;

import com.pearadmin.common.constant.MessageConstants;
import com.pearadmin.common.tools.serial.SnowFlake;
import com.pearadmin.common.web.base.BaseController;
import com.pearadmin.common.web.domain.ResuBean;
import com.pearadmin.common.web.domain.ResuTable;
import com.pearadmin.common.web.domain.ResuTree;
import com.pearadmin.system.domain.SysPower;
import com.pearadmin.system.service.ISysPowerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("system/power")
public class SysPowerController extends BaseController {

    /**
     * Describe: 基础路径
     * */
    private static String MODULE_PATH = "system/power/";

    /**
     * Describe: 权限模块服务
     * */
    @Resource
    private ISysPowerService sysPowerService;

    /**
     * Describe: 获取权限列表视图
     * Param ModelAndView
     * Return 权限列表视图
     * */
    @GetMapping("main")
    @PreAuthorize("hasPermission('/system/power/main','sys:power:main')")
    public ModelAndView main(ModelAndView modelAndView){
        modelAndView.setViewName(MODULE_PATH + "main");
        return modelAndView;
    }

    /**
     * Describe: 获取权限列表数据
     * Param ModelAndView
     * Return 权限列表数据
     * */
    @GetMapping("data")
    public ResuTable data(SysPower sysPower){
        return treeTable(sysPowerService.list(sysPower));
    }

    /**
     * Describe: 获取权限新增视图
     * Param ModelAndView
     * Return 权限新增视图
     * */
    @GetMapping("add")
    public ModelAndView add(ModelAndView modelAndView){
        modelAndView.setViewName(MODULE_PATH + "add");
        return modelAndView;
    }

    /**
     * Describe: 获取权限修改视图
     * Param ModelAndView
     * Return 权限修改视图
     * */
    @GetMapping("edit")
    public ModelAndView edit(ModelAndView modelAndView,String powerId){
        modelAndView.addObject("sysPower",sysPowerService.getById(powerId));
        modelAndView.setViewName(MODULE_PATH + "edit");
        return modelAndView;
    }

    /**
     * Describe: 保存权限信息
     * Param: SysPower
     * Return: ResuBean
     * */
    @PostMapping("save")
    public ResuBean save(@RequestBody SysPower sysPower){
        sysPower.setPowerId(""+new SnowFlake().nextId());
        boolean result = sysPowerService.save(sysPower);
        return decide(
                result,                           // 响应结果
                MessageConstants.SAVE_SUCCESS,     // 成功消息
                MessageConstants.SAVE_FAILURE      // 失败消息
        );
    }

    /**
     * Describe: 修改权限信息
     * Param SysPower
     * Return 执行结果
     * */
    @PutMapping("update")
    public ResuBean update(@RequestBody SysPower sysPower){
        boolean result = sysPowerService.update(sysPower);
        return decide(
                result,                           // 响应结果
                MessageConstants.UPDATE_SUCCESS,   // 成功消息
                MessageConstants.UPDATE_FAILURE    // 失败消息
        );
    }

    /**
     * Describe: 根据 id 进行删除
     * Param id
     * Return ResuTree
     * */
    @DeleteMapping("remove/{id}")
    public ResuBean remove(@PathVariable String id){
        boolean result = sysPowerService.remove(id);
        return decide(
                result,                           // 响应结果
                MessageConstants.REMOVE_SUCCESS,   // 成功消息
                MessageConstants.REMOVE_FAILURE    // 失败消息
        );
    }

    /**
     * Describe: 获取父级权限选择数据
     * Param sysPower
     * Return ResuTree
     * */
    @GetMapping("selectParent")
    public ResuTree selectParent(SysPower sysPower){
        List<SysPower> list = sysPowerService.list(sysPower);
        SysPower basePower = new SysPower();
        basePower.setPowerName("顶级权限");
        basePower.setPowerId("0");
        basePower.setParentId("-1");
        list.add(basePower);
        return dataTree(list);
    }

    /**
     * Describe: 根据 Id 开启用户
     * Param powerId
     * Return ResuTree
     * */
    @PutMapping("enable")
    public ResuBean enable(@RequestBody SysPower sysPower){
        sysPower.setEnable("0");
        boolean result = sysPowerService.update(sysPower);
        return decide(
                result,                            // 响应结果
                MessageConstants.UPDATE_SUCCESS,   // 成功消息
                MessageConstants.UPDATE_FAILURE    // 失败消息
        );
    }

    /**
     * Describe: 根据 Id 禁用用户
     * Param powerId
     * Return ResuTree
     * */
    @PutMapping("disable")
    public ResuBean disable(@RequestBody SysPower sysPower){
        sysPower.setEnable("1");
        boolean result = sysPowerService.update(sysPower);
        return decide(
                result,                            // 响应结果
                MessageConstants.UPDATE_SUCCESS,   // 成功消息
                MessageConstants.UPDATE_FAILURE    // 失败消息
        );
    }
}