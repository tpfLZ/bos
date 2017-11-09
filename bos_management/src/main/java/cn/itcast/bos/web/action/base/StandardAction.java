package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.IStandardService;

@Controller("standardAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Actions
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard standard = new Standard();

    @Autowired
    @Qualifier("standardService")
    private IStandardService standardService;

    @Override
    public Standard getModel() {
        // TODO Auto-generated method stub
        return standard;
    }

    @Action(value = "standard_save", results = {
            @Result(name = "success", location = "./pages/base/standard.html", type = "redirect") })
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }
}