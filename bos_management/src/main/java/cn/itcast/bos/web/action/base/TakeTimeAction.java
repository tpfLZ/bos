package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.util.List;

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

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.ITakeTimeService;

@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller("takeTimeAction")
public class TakeTimeAction extends ActionSupport implements ModelDriven<TakeTime> {

    private TakeTime takeTime = new TakeTime();

    @Autowired
    @Qualifier("takeTimeService")
    private ITakeTimeService takeTimeService;

    @Override
    public TakeTime getModel() {
        // TODO Auto-generated method stub
        return takeTime;
    }

    static {
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
    }

    // 查询所有的排班时间
    @Action(value = "takeTime_findAll")
    public void findAll() {
        List<TakeTime> list = takeTimeService.findAll();
        String json = JSONObject.toJSONString(list);
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
