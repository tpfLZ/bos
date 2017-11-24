package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.take_delivery.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("promotionAction")
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends ActionSupport implements ModelDriven<Promotion> {

    static {
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
    }

    private Promotion promotion = new Promotion();
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public Promotion getModel() {
        // TODO Auto-generated method stub
        return promotion;
    }

    @Action(value = "promotion_pageQuery")
    public void pageQuery() {

        // 基于webservice获取bos-management活动列表对象
        PageBean<Promotion> pageBean = WebClient
                .create("http://localhost:8080/bos_management/services/promotionService/pageQuery?page=" + page
                        + "&rows=" + rows)
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(PageBean.class);

        // 将数据封装从json返回
        String json = JSONObject.toJSONString(pageBean);
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Action(value = "promotion_showDetail")
    public void showDetail() throws IOException, TemplateException {
        // 先判断id对应的html是否存在，如果存在直接返回
        String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
        File htmlFile = new File(htmlRealPath + "/" + promotion.getId() + ".html");
        // 如果html不存在，查询数据库，结合freemarker技术生成网页模板
        if (!htmlFile.exists()) {
            // 配置对象，配置模板位置
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            configuration.setDirectoryForTemplateLoading(
                    new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
            // 获取模板对象
            Template template = configuration.getTemplate("promotion_detail.ftl");
            // 动态数据
            Promotion promotionData = WebClient
                    .create("http://localhost:8080/bos_management/services/promotionService/promotion/"
                            + promotion.getId())
                    .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("promotion", promotionData);
            // 合并输出
            template.process(parameterMap, new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));
        }
        // 存在直接将文件返回
        ServletActionContext.getResponse().setContentType("text/html:charset=utf-8");
        FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
    }

}
