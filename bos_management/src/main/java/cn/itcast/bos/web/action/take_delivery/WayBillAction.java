package cn.itcast.bos.web.action.take_delivery;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.IWayBillService;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("wayBillAction")
@Scope("prototype")
public class WayBillAction extends ActionSupport implements ModelDriven<WayBill> {

    static {
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
    }

    // 保存日志
    private static final Logger LOGGER = Logger.getLogger(WayBill.class);

    private WayBill wayBill = new WayBill();
    @Autowired
    @Qualifier("wayBillService")
    private IWayBillService wayBillService;

    @Override
    public WayBill getModel() {
        return wayBill;
    }

    @Action(value = "waybill_save")
    public void save() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (wayBill.getOrder() != null && (wayBill.getOrder().getId() == null || wayBill.getOrder().getId() == 0)) {
                wayBill.setOrder(null);
            }
            wayBillService.save(wayBill);
            result.put("success", true);
            result.put("msg", "保存运单成功！");
            LOGGER.info("保存成功，运单号：" + wayBill.getWayBillNum());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "保存运单失败！");
            LOGGER.error("保存失败，运单号：" + wayBill.getWayBillNum(), e);
        }
        String json = JSONObject.toJSONString(result);
        ServletActionContext.getResponse().getWriter().write(json);
    }

    // 快速运单分页查询
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "waybill_pageQuery")
    public void pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
        Page<WayBill> pageData = wayBillService.findPageData(pageable);
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

    // 快速运单回显
    @Action(value = "waybill_findByWayBillNum")
    public void findByWayBillNum() {
        WayBill wayBillData = wayBillService.findWayBill(wayBill.getWayBillNum());
        Map<String, Object> result = new HashMap<String, Object>();
        if (wayBillData == null) {
            result.put("success", false);
        } else {
            result.put("success", true);
            result.put("wayBillData", wayBillData);
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
