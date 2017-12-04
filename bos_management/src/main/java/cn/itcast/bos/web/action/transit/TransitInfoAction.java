package cn.itcast.bos.web.action.transit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.ITransitInfoService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class TransitInfoAction extends ActionSupport implements ModelDriven<TransitInfo> {

    private TransitInfo transitInfo = new TransitInfo();

    @Autowired
    @Qualifier("transitInfoService")
    private ITransitInfoService transitInfoService;

    @Override
    public TransitInfo getModel() {
        return transitInfo;
    }

    private String wayBillIds;

    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }

    @Action(value = "transit_create")
    public void create() {
        // 调用业务层，保存transitinfo信息
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            transitInfoService.createTransits(wayBillIds);
            result.put("success", true);
            result.put("msg", "开启中转配送成功！");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "开启中转配送失败！" + e.getMessage());
        }
        String json = JSONObject.toJSONString(result);
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
