package org.example.employeeleaveform.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.employeeleaveform.dao.EmployeeDao;
import org.example.employeeleaveform.dao.LeaveFormDao;
import org.example.employeeleaveform.dto.EmployeeDto;
import org.example.employeeleaveform.dto.EmployeeLeaveFormDto;
import org.example.employeeleaveform.entity.Employee;
import org.example.employeeleaveform.utils.EntityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public List<EmployeeLeaveFormDto> findEmployeeFormByNameOrEmail(String nameOrEmail) {
        return   employeeDao
                .findEmployeesByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(nameOrEmail, nameOrEmail)
                .stream().map(EntityUtil::toEmployeeLeaveFormDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeLeaveFormDto> findAllEmployees() {
        return employeeDao
                .findAll()
                .stream()
                .map(EntityUtil::toEmployeeLeaveFormDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        return EntityUtil
                .employeeToDto(employeeDao.save
                        (EntityUtil.employeeDtoToEmployee(employeeDto)
                        ));
    }

    public void deleteEmployee(int empId) {
        if (employeeDao.existsById(empId)) {
            employeeDao.deleteById(empId);
        } else

            throw new EntityNotFoundException("Employee with id " + empId + "is not found");
    }

    public EmployeeLeaveFormDto findEmployeeById(int empId) {
        Employee employee = employeeDao
                .findEmployeeById(empId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + empId + "is not found"));
        return EntityUtil.toEmployeeLeaveFormDto(employee);
    }



    public List<EmployeeLeaveFormDto> findAllLeaveForms() {
        return employeeDao
                .findAll()
                .stream()
                .map(EntityUtil::toEmployeeLeaveFormDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
