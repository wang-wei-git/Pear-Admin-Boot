package com.pearadmin.process.controller;

import com.pearadmin.common.web.base.BaseController;
import com.pearadmin.common.web.domain.request.PageDomain;
import com.pearadmin.common.web.domain.response.Result;
import com.pearadmin.common.web.domain.response.ResultTable;
import com.pearadmin.process.domain.ProDefined;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/process/defined")
public class ProDefinedController extends BaseController {

    private String modelPath = "process/defined/";

    @Resource
    private RepositoryService repositoryService;

    @GetMapping("main")
    public ModelAndView main(){
        return JumpPage(modelPath + "main");
    }

    @GetMapping("data")
    public ResultTable data(PageDomain pageDomain){

        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .listPage(pageDomain.start(),pageDomain.end());

        List<ProDefined> data = new ArrayList<>();

        processDefinitions.forEach(processDefinition -> {
            ProDefined defined = new ProDefined();
            defined.setId(processDefinition.getId());
            defined.setName(processDefinition.getName());
            defined.setVersion(processDefinition.getVersion());
            defined.setKey(processDefinition.getKey());
            defined.setBpmn(processDefinition.getResourceName());
            defined.setPng(processDefinition.getDiagramResourceName());
            defined.setDeploymentId(processDefinition.getDeploymentId());
            data.add(defined);
        });

        return pageTable(data,repositoryService
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .count());
    }

    @DeleteMapping("remove/{deploymentId}")
    public Result remove(@PathVariable String deploymentId){
        repositoryService.deleteDeployment(deploymentId,true);
        return Result.success("删除成功");
    }


    private InputStream getProcessDefineResource(String processDefineId, String resourceName) {

        return repositoryService.getResourceAsStream(processDefineId, resourceName);
    }

    @GetMapping("/png")
    public void getProcessDefineImge(HttpServletResponse response,
                                     @RequestParam("definedId") String processDefineId,String resourceName){
        InputStream inputStream = getProcessDefineResource(processDefineId, resourceName);
        byte[] bytes = new byte[1024];
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/xml")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("definedId") String processDefineId,String resourceName){
        InputStream inputStream = getProcessDefineResource(processDefineId, resourceName);
        byte[] bytes = new byte[1024];
        response.setContentType("text/xml");
        try {
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
