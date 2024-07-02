package org.example.employeeleaveform.dao;

import org.example.employeeleaveform.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee, Integer> {
    Optional<Employee> findEmployeeById(int id);

    List<Employee> findEmployeesByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nameOrEmail, String nameOrEmail1);
}
