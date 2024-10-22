package com.devsuperior.demo.projections;

public interface EmployeesDetailsProjection {
    Long getEmployeeId();
    String getEmployeeName();
    String getEmployeeSalary();
    Long getDepartmentId();
    String getDepartmentName();
}
