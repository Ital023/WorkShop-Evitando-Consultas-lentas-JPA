package com.devsuperior.demo.repositories;

import com.devsuperior.demo.projections.EmployeesDetailsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.demo.entities.Employee;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT obj FROM Employee obj JOIN FETCH obj.department")
    List<Employee> searchAll();

    @Query(value = "SELECT obj FROM Employee obj JOIN FETCH obj.department",
    countQuery = "SELECT COUNT(obj) FROM Employee obj JOIN obj.department")
    Page<Employee> searchAll(Pageable pageable);

    @Query(nativeQuery = true,
    value = """
            SELECT employee.id, employee.name, employee.salary, department.id, department.name 
            FROM TB_EMPLOYEE as employee
            INNER JOIN TB_DEPARTMENT as department ON employee.id = department.id
            """)
    List<EmployeesDetailsProjection> searchAllQuerySQL();

}
