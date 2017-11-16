package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.ICustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.ICustomerService;

@Service("customerService")
@Transactional
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public List<Customer> findNoAssociationCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    // 因为发过来的是定区的id和所有要关联的客户的id和已经关联的客户的id，然后考虑到又要解除一部分客户的关联，所以首先将
    // 表中所有指定定区id置空，然后将要关联的客户根据发过来的id和定区id进行关联，这样就同时做到了解除部分关联客户和关联部分客户
    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
        // 解除关联动作
        customerRepository.clearFixedAreaId(fixedAreaId);
        // 分割字符串
        if (StringUtils.isBlank(customerIdStr)) {
            return;
        }
        String[] customerIdArray = customerIdStr.split(",");
        for (String idStr : customerIdArray) {
            Integer id = Integer.parseInt(idStr);
            customerRepository.updateFixedAreaId(fixedAreaId, id);
        }
    }

}
