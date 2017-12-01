package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.IWayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.IWayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.IWayBillService;

@Service("wayBillService")
@Transactional
public class WayBillService implements IWayBillService {

    @Autowired
    private IWayBillRepository wayBillRepository;
    @Autowired
    private IWayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill wayBill) {
        // 判断运单号是否存在
        WayBill persistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (persistWayBill == null || persistWayBill.getId() == null) {
            // 运单不存在
            wayBillRepository.save(wayBill);
            wayBillIndexRepository.save(wayBill);
        } else {
            // 运单存在
            try {
                Integer id = persistWayBill.getId();
                // 将前台传过来的运单详情赋值给之前相同的快速运单，但是要保持id不能变，不然就成了一条新数据
                BeanUtils.copyProperties(persistWayBill, wayBill);
                persistWayBill.setId(id);
                // 保存索引
                wayBillIndexRepository.save(wayBill);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable) {
        // 判断waybill中的条件是否存在
        if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == 0 || wayBill.getSignStatus() == null)) {
            // 无条件查询，查询数据库
            return wayBillRepository.findAll(pageable);
        } else {
            // 查询条件
            // must条件必须成立and
            // must not 条件必须不成立mot
            // should条件也许成立or
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();// 布尔查询，多条件组合查询
            // 向组合查询对象添加条件
            if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
                // 运单号查询
                QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                boolQueryBuilder.mustNot(tempQuery);
            }
            if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
                // 发货地模糊查询
                // 情况一：输入“北”是查询词条的一部分，使用模糊匹配词条查询
                QueryBuilder wildcardQuery =
                        new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");
                // 情况二：输入“北京市海淀区”是个多次条组合，进行分词后每个词条查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress())
                        .field("sendAddress").defaultOperator(Operator.AND);
                // 两种情况取或关系
                BoolQueryBuilder query = new BoolQueryBuilder();
                query.should(wildcardQuery);
                query.should(queryStringQueryBuilder);

                boolQueryBuilder.must(query);
            }
            if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
                // 收货地模糊查询
                QueryBuilder wildcardQuery =
                        new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                boolQueryBuilder.must(wildcardQuery);
            }
            if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
                // 速运类型等值查询
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                boolQueryBuilder.must(termQuery);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                // 签收状态等值查询
                QueryBuilder termQuery = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                boolQueryBuilder.must(termQuery);
            }
            SearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder);
            // 分页效果
            searchQuery.setPageable(pageable);
            return wayBillIndexRepository.search(searchQuery);
        }
    }

    @Override
    public WayBill findWayBill(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

}
