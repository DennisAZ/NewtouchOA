var flowrun_new_left = {spanText:[['span1','请选择工作流程']]};
var flowrun_new_flowtop = {spanText:[['span1','常用工作流程'],['span2','上次建立的工作'],['spanMsg','没有工作']]};
var flowrun_new_edit = {spanText:[['span1','填写该工作的名称或文号']]};
var flowrun_list_index = {tabTitle:['未办理','办理中','我已办结','全部工作']};
var flowrun_list_index1 = {
    spanText:[['span1','工作名称/文号'],['span2','暂无待办工作'],['span3','所有流程类型']],
    tooltipMsg:[['tooltipMsg1','所选流程暂无待办工作'],['tooltipMsg2','无待办工作'],['tooltipMsg3','下一步骤尚未接收时可收回至本步骤重新办理，确认要收回吗？'],['tooltipMsg4','确认要结束该工作流程吗？'],['tooltipMsg5','确认要删除该工作流程吗？']]         
}
var flowrun_list_inputform_index = {title:'工作办理'}
var flowrun_list_inputform_operate = {
    spanText:[['span1','关注此工作']],
    tooltipMsg:[['tooltipMsg1','确认要关注此工作吗？']]
}
var flowrun_list_inputform_main = {
    tooltipMsg:[['tooltipMsg1','确认该工作已经办理完毕么？']
                 ,['tooltipMsg2','确认不保存此工作吗？']
                   ,['tooltipMsg3','此工作已被以下人员关注']
                     ,['tooltipMsg4','此工作没有人员关注']
                       ,['tooltipMsg5','确认要结束该工作流程吗？']
                         ,['tableTitle','表单']
                           ,['feedbackTitle','会签']
                             ,['dispAipTitle','签批']
                   ]
}
var flowrun_list_turn_turnnext = {
    title:'工作办结',
    tooltipMsg:[['tooltipMsg1','工作流结束提醒：'],['tooltipMsg2','工作流转交提醒：']]
}
var flowrun_query_index = {
    title:'工作流查询'
    ,spanText:[['span1','所有流程类型']]
    ,tooltipMsg:[['tooltipMsg1','工作名称/文号']
                  ,['tooltipMsg2','删除工作，请至少选择其中一项！']
                    ,['tooltipMsg3','确认要删除所选工作吗？']
                      ,['tooltipMsg4','结束工作，请至少选择其中一项！']
                        ,['tooltipMsg5',"确认要结束所选工作吗？"]
                          ,['tooltipMsg6',"确认要关注此工作吗？"]
                            ,['tooltipMsg7',"确认要取消关注此工作吗？"]
                              ,['tooltipMsg8',"要导出工作，请至少选择其中一项。"]
                    ]
    
}
var flowrun_manage_index = {
    title:'工作监控'
    ,spanText:[['span1','所有流程类型']]
    ,tooltipMsg:[['tooltipMsg1','工作名称/文号']
                  ,['tooltipMsg2',"确认要删除当前工作吗？"]
                    ,['tooltipMsg3',"确认要结束当前工作吗？"]
                    ]
}
var flowrun_overtime_index = {
    title:'超时统计查询',
    tabTitle:['超时工作查询','超时工作统计']
}
var flowrun_overtime_overquery = {
    spanText:[['span1','所有流程类型'],['span2','工作接收时间: 从']]
}
var flowrun_overtime_overtotal = {
    spanText:[['span1','工作接收时间:']]
}
var flowrun_overtime_viewDetail = {
    title:'超时工作列表',
    tooltipMsg:[['tooltipMsg1','工作名称/文号']]
}
var flowrun_rule_index = {title:'工作委托'}
var flowrun_rule_edit = {
    spanText:[['span1','编辑工作委托规则']]
}
var flowrun_rule_to = {
    spanText:[['span1','被委托工作记录']]
    ,tooltipMsg:[['tooltipMsg1','工作名称/文号']]
}
var flowrun_rule_from = {
    spanText:[['span1','已委托工作记录 ']]
    ,tooltipMsg:[['tooltipMsg1','工作名称/文号']]
}
var flowrun_destroy_index = {
    spanText:[['span1',' 可以查询到系统中所有已经删除的工作（不局限于自己经办的），然后进行彻底销毁或还原操作']
               ,['span2' ,'工作销毁与还原']
                 ,['span3','工作名称(关键字)：']
                   ,['span5','已删除工作查询结果']
                   ]
   ,tooltipMsg:[['tooltipMsg1','确认要销毁指定范围内的所有工作吗？删除后将不可恢复，确认删除请输入大写字母“OK”']
                 ,['tooltipMsg2',"工作销毁失败删除失败："]
                   ,['tooltipMsg3',"确认要恢复指定范围内的所有工作吗？确认还原请输入大写字母“OK”"]
                     ,['tooltipMsg4', "工作销毁失败删除失败："]
                       ,['tooltipMsg5','请选择一条要销毁的工作！']
                        ,['tooltipMsg6',"确认要永久销毁所选工作吗？"]
                          ,['tooltipMsg7',"销毁工作成功！"]
                            ,['tooltipMsg8', "销毁工作失败："]
                              ,['tooltipMsg9',"确认要永久销毁所选工作吗？"]
                                ,['tooltipMsg10',"销毁工作失败："]
                                  ,['tooltipMsg11', "要还原工作，请至少选择其中一项。"]
                                    ,['tooltipMsg12',"确认要将所选工作恢复到执行中吗？"]
                                      ,['tooltipMsg13',"确认要将所选工作恢复到执行中吗？"]
                   ]
}
var flowrun_destroy_search = {
    spanText:[['span5','已删除工作查询结果']]
   ,tooltipMsg:[['tooltipMsg1','确认要销毁指定范围内的所有工作吗？删除后将不可恢复，确认删除请输入大写字母“OK”']
                 ,['tooltipMsg2',"工作销毁失败删除失败："]
                   ,['tooltipMsg3',"确认要恢复指定范围内的所有工作吗？确认还原请输入大写字母“OK”"]
                     ,['tooltipMsg4', "工作销毁失败删除失败："]
                       ,['tooltipMsg5','请选择一条要销毁的工作！']
                        ,['tooltipMsg6',"确认要永久销毁所选工作吗？"]
                          ,['tooltipMsg7',"销毁工作成功！"]
                            ,['tooltipMsg8', "销毁工作失败："]
                              ,['tooltipMsg9',"确认要永久销毁所选工作吗？"]
                                ,['tooltipMsg10',"销毁工作失败："]
                                  ,['tooltipMsg11', "要还原工作，请至少选择其中一项。"]
                                    ,['tooltipMsg12',"确认要将所选工作恢复到执行中吗？"]
                                      ,['tooltipMsg13',"确认要将所选工作恢复到执行中吗？"]
                                        ,['tooltipMsg14','工作名称/文号']
                   ]
}
var flowrun_log_search = {tooltipMsg:[['tooltipMsg1','工作名称/文号']]};
var flowrun_log_index = { spanText:[['span1','工作名称/文号：']]};
var flowrun_query_flowList = {
    spanText:[['span1','工作流高级查询 - 请选择流程']]
}
var flowrun_query_query= {
    spanText:[['span1','工作流高级查询 - 请指定查询条件'],['span2','工作流状态：'],['span3','工作流程基本属性']]
}
var flowrun_query_doQuery={spanText:[['span1','工作查询结果'],['span2','工作名称/文号']],
    tooltipMsg:[['tooltipMsg1',"确认要关注此工作吗？"]
                 ,['tooltipMsg2',"确认要取消关注此工作吗？"]
                   ,['tooltipMsg3','结束工作，请至少选择其中一项！']
                     ,['tooltipMsg4',"确认要结束所选工作吗？"]
                       ,['tooltipMsg5','删除工作，请至少选择其中一项！']
                         ,['tooltipMsg6',"确认要删除所选工作吗？"]
                           ,['tooltipMsg7',"要导出工作，请至少选择其中一项。"]]}
var flowrun_destroy_deleteMsrg = {tooltipMsg:[['tooltipMsg1',"操作已成功，共销毁工作"]]};
var flowrun_destroy_recoverMsrg = {tooltipMsg:[['tooltipMsg1', "操作已成功，共还原工作"]]};
var flowrun_list_allList = {
    spanText:[['span1','&nbsp;我的全部工作']]
}