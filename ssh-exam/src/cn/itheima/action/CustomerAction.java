package cn.itheima.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itheima.domain.Customer;
import cn.itheima.service.ICustomerService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    @Autowired
    @Qualifier("customerService")
    private ICustomerService customerService;
    private File cusImg;
    private String cusImgFileName;

    public File getCusImg() {
        return cusImg;
    }

    public void setCusImg(File cusImg) {
        this.cusImg = cusImg;
    }

    public String getCusImgFileName() {
        return cusImgFileName;
    }

    public void setCusImgFileName(String cusImgFileName) {
        this.cusImgFileName = cusImgFileName;
    }

    private Customer customer = new Customer();

    @Override
    public Customer getModel() {
        return customer;
    }

    @Action(value = "findAllCustomer", results = { @Result(name = "success", location = "/CustomerList.jsp") })
    public String findAllCustomer() {

        List<Customer> cs = customerService.findAllCustomer();
        ActionContext.getContext().getValueStack().set("cs", cs);

        return SUCCESS;
    }

    @Action(value = "addCustomer", results = {
            @Result(name = "success", location = "findAllCustomer", type = "redirectAction"),
            @Result(name = "input", location = "/error.jsp") })
    public String addCustomer() {
        File destFile = new File(ServletActionContext.getServletContext().getRealPath("/upload"), cusImgFileName);
        try {
            FileUtils.copyFile(cusImg, destFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Customer c = new Customer();
        c.setCusName(customer.getCusName());
        c.setCusPhone(customer.getCusPhone());
        String cusImgsrc = ServletActionContext.getRequest().getContextPath() + "/upload/" + cusImgFileName;
        c.setCusImgsrc(cusImgsrc);
        customerService.addCustomer(c);

        return SUCCESS;
    }

    @Action(value = "delCustomer", results = {
            @Result(name = "success", location = "findAllCustomer", type = "redirectAction") })
    public String delCustomer() {
        ServletActionContext.getRequest().getParameter("id");
        customerService.delCustomer(customer);
        return SUCCESS;
    }

}
