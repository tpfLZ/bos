package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.IAreaService;
import cn.itcast.bos.utils.PinYin4jUtils;

@Namespace("/")
@ParentPackage("struts-default")
@Controller("areaAction")
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {

    @Autowired
    @Qualifier("areaService")
    private IAreaService areaService;
    private File file;
    private Area area = new Area();

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public Area getModel() {
        // TODO Auto-generated method stub
        return area;
    }

    @Action(value = "area_batchImport")
    public String batchImport() throws IOException {
        List<Area> areas = new ArrayList<Area>();
        // 基于.xls格式解析HSSF
        // 1.加载Excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 读取一个sheet
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        // 读取sheet中的每一行
        for (Row row : hssfSheet) {
            // 一行数据对应一个区域对象
            if (row.getRowNum() == 0) {
                // 因为第一行是表头，就跳过
                continue;
            }
            // 跳过空行
            if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            // 封装数据
            Area area = new Area();
            area.setId(row.getCell(0).getStringCellValue());
            area.setProvince(row.getCell(1).getStringCellValue());
            area.setCity(row.getCell(2).getStringCellValue());
            area.setDistrict(row.getCell(3).getStringCellValue());
            area.setPostcode(row.getCell(4).getStringCellValue());
            // 基于pinyin4j生成城市编码和简码
            String province = area.getProvince();
            String city = area.getCity();
            String district = area.getDistrict();
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            district = district.substring(0, district.length() - 1);
            // 简码
            String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
            StringBuffer stringBuffer = new StringBuffer();
            for (String string : headArray) {
                stringBuffer.append(string);
            }
            String shortCode = stringBuffer.toString();
            area.setShortcode(shortCode);
            // 城市编码
            String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
            area.setCitycode(cityCode);
            areas.add(area);
        }
        // 调用业务层
        areaService.saveBatch(areas);
        return NONE;
    }

    // 实现区域的分页查询
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "area_pageQuery")
    public void pageQuery() {
        // 获取分页查询的数据
        Pageable pageable = new PageRequest(page - 1, rows);
        Specification<Area> specification = new Specification<Area>() {

            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(area.getProvince())) {
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + area.getProvince() + "%");
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(area.getCity())) {
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + area.getCity() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(area.getDistrict())) {
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + area.getDistrict() + "%");
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层获得相应的数据
        Page<Area> pageData = areaService.pageQuery(specification, pageable);
        // 将结果以指定的数据格式返回到浏览器
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageData.getTotalElements());
        map.put("rows", pageData.getContent());

        // 过滤掉不必要的属性
        PropertyFilter propertyFilter = new PropertyFilter() {

            @Override
            public boolean apply(Object object, String name, Object value) {
                if ("subareas".equalsIgnoreCase(name)) {
                    return false;
                }
                return true;
            }
        };
        String json = JSONObject.toJSONString(map, propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
        try {
            ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
