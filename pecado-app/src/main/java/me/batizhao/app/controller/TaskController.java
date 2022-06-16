package me.batizhao.app.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.App;
import me.batizhao.app.domain.AppForm;
import me.batizhao.app.domain.AppProcess;
import me.batizhao.app.service.AppFormService;
import me.batizhao.app.service.AppProcessService;
import me.batizhao.app.service.AppService;
import me.batizhao.app.view.InitApp;
import me.batizhao.common.core.domain.PecadoUser;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.R;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.terrace.api.TaskService;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.dto.StartProcessDTO;
import me.batizhao.terrace.dto.SubmitProcessDTO;
import me.batizhao.terrace.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 流程任务管理 API
 *
 * @module oa
 *
 * @author batizhao
 * @since 2021-06-18
 */
@Tag(name = "任务管理")
@RestController
@Slf4j
@Validated
public class TaskController {

    @Autowired
    private AppService appService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TerraceApi terraceApi;

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private AppProcessService appProcessService;

    /**
     * 通过应用Id初始化应用表单与组件默认值
     * @param key 表单key
     * @return R
     */
    @Operation(description = "通过表单key初始化应用表单与组件默认值")
    @GetMapping("/process/init")
    @PreAuthorize("isAuthenticated()")
    public R<InitApp> init(@RequestParam(name = "key") String key,
                           @RequestParam(name = "taskId", required = false, defaultValue = "") String taskId,
                           @RequestParam(name = "taskType", required = false, defaultValue = "") String taskType) {

        AppForm appForm = appFormService.getOne(Wrappers.<AppForm>lambdaQuery().eq(AppForm::getFormKey, key));
        if(appForm == null){
            throw new NotFoundException("表单不存在!");
        }
        App app = appService.getById(appForm.getAppId());

        InitApp initApp = new InitApp().setCode(app.getCode()).setName(app.getName());
        initApp.setAppForm(appForm);

        if(StringUtils.isNotBlank(taskId)){
            //初始化任务
            PecadoUser user = SecurityUtils.getUser();
            terraceApi.sign(taskId, user.getUsername(),StringUtils.isBlank(taskType) ? "0" : taskType);
            TaskNodeView tasks = terraceApi.loadTaskDetail(taskId, StringUtils.isBlank(taskType) ? "0" : taskType).getData();
            //根据流程配置动态加载业务处理表单
            TaskNodeView.Config config = tasks.getConfig();
            NodeConfig nodeConfig = config.getConfig();
            NodeConfig.Form formConfig = nodeConfig.getForm();
            String pcPath = formConfig.getPcPath();
            if(StringUtils.isNotBlank(pcPath)){
                AppForm newAppForm = appFormService.getOne(Wrappers.<AppForm>lambdaQuery().eq(AppForm::getFormKey, pcPath));
                if(newAppForm != null){
                    initApp.setAppForm(newAppForm);
                }
            }
            initApp.setTask(tasks);
        }else{
            //初始流程
            AppProcess queryAppProcess = new AppProcess();
            queryAppProcess.setFormId(appForm.getId());
            queryAppProcess.setStatus("open");

            List<AppProcess> appProcessList = appProcessService.findAppProcess(queryAppProcess);
            if(CollectionUtil.isNotEmpty(appProcessList)){
                AppProcess appProcess = appProcessList.get(0);
                InitProcessDefView process = terraceApi.loadProcessDefinitionByKey(appProcess.getProcessKey()).getData();
                initApp.setProcess(process);
            }
        }

        initApp.setAppForm(appForm);

        return R.ok(initApp);
    }

    /**
     * 获取流程定义
     * @param key 流程key
     * @return R
     * @real_return R<InitProcessDefView>
     */
    @Operation(description = "获取流程定义")
    @GetMapping(value = "/process/", params = "key")
    public R<InitProcessDefView> handleProcessDefinition(@Parameter(name = "key" , required = true) @RequestParam("key") @Size(min = 1) String key) {
        return R.ok(taskService.findProcessDefinitionByKey(key));
    }

    /**
     * 获取环节的输出路由及路由后的任务环节配置信息
     * @param processDefinitionId 流程定义Id
     * @param taskDefKey 流程环节key
     * @return R
     * @real_return R<List<ProcessRouterView>>
     */
    @Operation(description = "获取环节的输出路由及路由后的任务环节配置信息")
    @GetMapping(value = "/process/{processDefinitionId}/{taskDefKey}")
    public R<List<ProcessRouterView>> handleProcessRouter(@Parameter(name = "processDefinitionId" , required = true) @PathVariable("processDefinitionId") @Size(min = 1) String processDefinitionId,
                                                          @Parameter(name = "taskDefKey" , required = true) @PathVariable("taskDefKey") @Size(min = 1) String taskDefKey) {
        return R.ok(taskService.findProcessRouter(processDefinitionId, taskDefKey));
    }

    /**
     * 待办任务
     * @param appTodoTaskDTO
     * @return R
     * @real_return R<Page<TodoTaskView>>
     */
    @Operation(description = "待办任务")
    @GetMapping("/task/todos")
    public R<IPage<TodoTaskView>> handleTodoTasks(Page page, AppTodoTaskDTO appTodoTaskDTO) {
        PecadoUser user = SecurityUtils.getUser();
        appTodoTaskDTO.setUserName(user.getUsername());
        return R.ok(taskService.findTodoTasks(page, appTodoTaskDTO));
    }

    /**
     * 已办任务
     * @param appTodoTaskDTO
     * @return
     */
    @Operation(description = "已办任务")
    @GetMapping("/task/done")
    public R<IPage<TodoTaskView>> handleDoneTasks(Page page, AppTodoTaskDTO appTodoTaskDTO) {
        PecadoUser user = SecurityUtils.getUser();
        appTodoTaskDTO.setUserName(user.getUsername());
        return R.ok(taskService.findDoneTasks(page, appTodoTaskDTO));
    }

    /**
     * 通过id查询
     * @param id id
     * @return R<TaskNodeView>
     */
    @Operation(description = "通过id查询")
    @GetMapping(value = "/task", params = "id")
    public R<TaskNodeView> handleId(@Parameter(name = "ID" , required = true) @RequestParam("id") @Min(1) Long id) {
        return R.ok(taskService.findById(id));
    }

    /**
     * 流程提交
     * @param dto 流程提交对象
     * @return R<String>
     */
    @Operation(description = "流程提交")
    @PostMapping("/submit")
    public R<String> handleSubmit(@Valid @Parameter(name = "任务" , required = true) @RequestBody SubmitProcessDTO dto) {
        return R.ok(taskService.submit(dto));
    }

    /**
     * 流程启动
     * @param dto 启动对象
     * @return R<String>
     */
    @Operation(description = "流程启动")
    @PostMapping("/start")
    public R<String> handleStart(@Valid @Parameter(name = "任务" , required = true) @RequestBody StartProcessDTO dto) {
        PecadoUser user = SecurityUtils.getUser();
        dto.setUserId(user.getUsername());
        dto.setUserName(user.getUsername());
        dto.getDto().setCreator(user.getUsername());
        return R.ok(taskService.start(dto));
    }

    /**
     * 获取流程指定环节意见
     *
     * @param procInstId 流程实例Id
     * @param taskDefKeyList 指定环节
     * @param orderRule 排序规则 0 时间升序排， 1 先按人员职位排序，同级别时间升序排
     * @return R<List<ProcessMessageView>>
     */
    @Operation(description = "获取流程指定环节意见")
    @GetMapping("/comments")
    public R<List<ProcessMessageView>> handleComment(@Parameter(name = "procInstId", required = true) @RequestParam("procInstId") @Size(min = 1) String procInstId,
                                                     @Parameter(name = "taskDefKeyList", required = true) @RequestParam("taskDefKeyList") List<String> taskDefKeyList,
                                                     @Parameter(name = "orderRule", required = true) @RequestParam("orderRule") Integer orderRule) {
        return R.ok(taskService.loadMessage(procInstId, taskDefKeyList, orderRule));
    }

    /**
     * 签收
     * @param taskId 任务Id
     * @param type 任务类型：0 审批任务、 1 传阅任务
     * @return
     */
    @Operation(description = "签收")
    @PostMapping("/task/sign")
    public R<Boolean> handleSign(@Parameter(name = "taskId", required = true) @RequestParam("taskId") String taskId,
                                 @Parameter(name = "type") @RequestParam("type") String type) {
        return R.ok(taskService.sign(taskId, type, SecurityUtils.getUser().getUserId().toString()));
    }

    /**
     * 候选人
     * @param procInstId 流程实例Id
     * @param taskDefKey 流程定义Id
     * @param taskId 任务Id
     * @param back 是否退回
     * @param processDefId 流程定义Id
     * @param orgId 任务组织Id
     * @return
     */
    @Operation(description = "获取候选人")
    @GetMapping("/candidate")
    public R<List<QueryCandidateView>> candidate(@Parameter(name = "procInstId") String procInstId,
                                @Parameter(name = "taskDefKey") String taskDefKey,
                                @Parameter(name = "taskId") String taskId,
                                @Parameter(name = "back") Boolean back,
                                @Parameter(name = "processDefId") String processDefId,
                                @Parameter(name = "orgId") String orgId) {
        return R.ok(taskService.loadCandidate(procInstId == null ? "0":procInstId, taskDefKey, taskId, back != null && back, processDefId, orgId));
    }

    /**
     * 获取流程资源
     * @param processDefId 流程定义Id
     * @param sourceType 资源类型
     * @return
     */
    @Operation(description = "获取流程资源")
    @GetMapping("/repository/resource")
    public R<byte[]> processResource(@Parameter(name = "processDefId") String processDefId,
                                                 @Parameter(name = "sourceType") String sourceType) {
        return R.ok(taskService.loadProcessResource(processDefId, sourceType));
    }
}
