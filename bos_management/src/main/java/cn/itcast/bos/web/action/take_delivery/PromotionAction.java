package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.IPromotionService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("promotionAction")
@Scope("prototype")
public class PromotionAction extends ActionSupport implements ModelDriven<Promotion> {

    private Promotion promotion = new Promotion();

    static {
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
    }

    @Autowired
    @Qualifier("promotionService")
    private IPromotionService promotionService;

    @Override
    public Promotion getModel() {
        // TODO Auto-generated method stub
        return promotion;
    }

    // 保存宣传任务
    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Action(value = "promotion_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
    public String save() throws IOException {
        // 宣传图片在数据库中的保存路径
        String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
        System.out.println(ServletActionContext.getRequest().getRealPath("upload"));
        // 宣传图片在服务器中保存的路径
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
        // 生成随机图片名
        UUID uuid = UUID.randomUUID();
        String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
        String randomFileName = uuid + ext;
        // 保存图片（绝对路径名）
        File destFile = new File(savePath, randomFileName);
        FileUtils.copyFile(titleImgFile, destFile);
        // 将图片的相对路径保存到实体对象中
        promotion.setTitleImg(saveUrl + randomFileName);
        // 调用业务层保存相应的数据
        promotionService.save(promotion);
        return SUCCESS;
    }

    // 分页查询宣传任务
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "promotion_pageQuery")
    public void pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionService.findPageData(pageable);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        String json = JSONObject.toJSONString(result);
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
