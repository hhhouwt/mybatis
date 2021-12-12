package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface EmployeeMapper {
    Employee getEmpByMap(Map<String, Object> map);
    Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);
    Employee getEmpById(Integer id);
    Long addEmp(Employee employee);
    void updateEmp(Employee employee);
    Long deleteEmp(Integer id);
}
