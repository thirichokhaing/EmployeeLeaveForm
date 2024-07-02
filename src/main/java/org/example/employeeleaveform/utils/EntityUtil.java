package org.example.employeeleaveform.utils;

import org.example.employeeleaveform.dto.EmployeeDto;
import org.example.employeeleaveform.dto.EmployeeLeaveFormDto;
import org.example.employeeleaveform.dto.LeaveFormDto;
import org.example.employeeleaveform.entity.Employee;
import org.example.employeeleaveform.entity.LeaveForm;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class EntityUtil {
    public static Employee toEmployee(EmployeeLeaveFormDto employeeLeaveFormDto) {
        return new Employee(
                employeeLeaveFormDto.getName(),
                employeeLeaveFormDto.getPhoneNumber(),
                employeeLeaveFormDto.getEmail(),
                employeeLeaveFormDto.getNrc(),
                employeeLeaveFormDto.getDateOfBirth(),
                employeeLeaveFormDto.getGender(),
                employeeLeaveFormDto.getAddress()
        );
    }


    public static EmployeeDto toEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                employee.getNrc(),
                employee.getDateOfBirth(),
                employee.getGender(),
                employee.getAddress()
        );
    }
    public static LeaveFormDto toLeaveFormDto(LeaveForm leaveForm) {

       LeaveFormDto leaveFormDto = new LeaveFormDto();
       leaveFormDto.setId(leaveForm.getId());
       leaveFormDto.setFromDate(leaveForm.getFromDate());
       leaveFormDto.setToDate(leaveForm.getToDate());
       leaveFormDto.setLeaveType(leaveForm.getLeaveType());
       if(leaveForm.getEmployee() != null) {
           EmployeeDto employeeDto = toEmployeeDto(leaveForm.getEmployee());
           leaveFormDto.setEmployeeId(employeeDto.getId());
       }else{

           leaveFormDto.setEmployeeId(1);
       }
       return leaveFormDto;
    }

    public static List<LeaveFormDto> toLeaveFormDtoList(Employee employee) {
        return employee
                .getLeaveForms()
                .stream()
                .map(EntityUtil::toLeaveFormDto)
                .collect(Collectors.toList());

    }
    public static List<LeaveForm> toLeaveFormList(EmployeeLeaveFormDto employeeLeaveFormDto){
        return employeeLeaveFormDto
                .getLeaveForms()
                .stream()
                .map(form -> new LeaveForm(form.getFromDate(),form.getToDate(),form.getLeaveType()))
                .collect(Collectors.toList());

    }


    public static LeaveForm toLeaveForm(LeaveFormDto leaveFormDto) {
        return new LeaveForm(
                leaveFormDto.getFromDate(),
                leaveFormDto.getToDate(),
                leaveFormDto.getLeaveType()


        );
    }


    public static EmployeeLeaveFormDto toEmployeeLeaveFormDto(Employee employee){
        return new EmployeeLeaveFormDto(
                employee.getId(),
                employee.getName(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                employee.getNrc(),
                employee.getDateOfBirth(),
                employee.getGender(),
                employee.getAddress(),
                employee.getLeaveForms()
                        .stream()
                        .map(EntityUtil::toLeaveFormDto)
                        .collect(Collectors.toList())
        );

    }


    public static EmployeeDto employeeToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        BeanUtils.copyProperties(employee, employeeDto);
        return employeeDto;
    }

    public static Employee employeeDtoToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDto, employee);
        return employee;
    }
}
