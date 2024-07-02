package org.example.employeeleaveform.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.employeeleaveform.dao.EmployeeDao;
import org.example.employeeleaveform.dao.LeaveFormDao;
import org.example.employeeleaveform.dto.EmployeeDto;
import org.example.employeeleaveform.dto.EmployeeLeaveFormDto;
import org.example.employeeleaveform.dto.LeaveFormDto;
import org.example.employeeleaveform.entity.Employee;
import org.example.employeeleaveform.entity.LeaveForm;
import org.example.employeeleaveform.utils.EntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeLeaveFormService {

    private final EmployeeDao employeeDao;
    private final LeaveFormDao leaveFormDao;

    @Transactional
    public EmployeeLeaveFormDto createEmployeeLeaveForm(EmployeeLeaveFormDto employeeLeaveFormDto) {
        Employee employee = employeeDao.save(EntityUtil.toEmployee(employeeLeaveFormDto));
        List<LeaveForm> leaveForms = EntityUtil.toLeaveFormList(employeeLeaveFormDto);
        for (LeaveForm form : leaveForms) {
            employee.addLeaveForm(form);
        }
        Employee saveEmployee = employeeDao.save(employee);
        EmployeeLeaveFormDto formDto = new EmployeeLeaveFormDto();
        formDto.setEmployeeId(saveEmployee.getId());
        return formDto;
    }


    public EmployeeLeaveFormDto findEmployeeLeaveFormById(int id) {
        return employeeDao
                .findEmployeeById(id)
                .map(EntityUtil::toEmployeeLeaveFormDto)
                .orElseThrow(() -> new EntityNotFoundException("Entity Not Found"));
    }

    @Transactional
    public void deleteLeaveForm(int empId) {
        LeaveForm leaveForm = leaveFormDao.findLeaveFormByEmployeeId(empId)
                .orElseThrow(() -> new EntityNotFoundException("LeaveForm with id " + empId + "is not found"));
        if (leaveForm != null) {
            leaveFormDao.deleteLeaveFormByEmployeeId(empId);
            System.out.println("===================Id===========" + empId);
        } else
            throw new EntityNotFoundException("Leave form with id " + empId + "is not found");
    }

    public void deleteAllEmployeeLeaveFormById(int id) {
        if (employeeDao.existsById(id)) {
            employeeDao.deleteById(id);
        } else
            throw new EntityNotFoundException("Id Not Found");
    }


    @Transactional
    public EmployeeLeaveFormDto updateForm(EmployeeLeaveFormDto employeeLeaveFormDto, int id) {
        EmployeeLeaveFormDto oldDto = findEmployeeLeaveFormById(id);
        oldDto.setEmployeeId(id);
        oldDto.setName(employeeLeaveFormDto.getName());
        oldDto.setPhoneNumber(employeeLeaveFormDto.getPhoneNumber());
        oldDto.setEmail(employeeLeaveFormDto.getEmail());
        oldDto.setNrc(employeeLeaveFormDto.getNrc());
        oldDto.setDateOfBirth(employeeLeaveFormDto.getDateOfBirth());
        oldDto.setGender(employeeLeaveFormDto.getGender());
        oldDto.setAddress(employeeLeaveFormDto.getAddress());
        oldDto.setLeaveForms(employeeLeaveFormDto.getLeaveForms());

        Employee updatedEmployee = updateEmployee(oldDto);

        EmployeeLeaveFormDto formDto = new EmployeeLeaveFormDto();

        formDto.setEmployeeId(updatedEmployee.getId());
        formDto.setName(updatedEmployee.getName());
        formDto.setPhoneNumber(updatedEmployee.getPhoneNumber());
        formDto.setEmail(updatedEmployee.getEmail());
        formDto.setNrc(updatedEmployee.getNrc());
        formDto.setDateOfBirth(updatedEmployee.getDateOfBirth());
        formDto.setGender(updatedEmployee.getGender());
        formDto.setAddress(updatedEmployee.getAddress());

        List<LeaveFormDto> leaveFormDtoList = updatedEmployee
                .getLeaveForms()
                .stream()
                .map(EntityUtil::toLeaveFormDto)
                .collect(Collectors.toList());

        formDto.setLeaveForms(leaveFormDtoList);

        return formDto;
    }

    @Transactional
    public Employee updateEmployee(EmployeeLeaveFormDto employeeLeaveFormDto) {
        Employee existingEmployee = employeeDao.findById(employeeLeaveFormDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee Not found"));
        updateNewEmployeeValue(employeeLeaveFormDto, existingEmployee);

        List<LeaveForm> updatedLeaveForms = new ArrayList<>();
        for (LeaveFormDto leaveFormDto : employeeLeaveFormDto.getLeaveForms()) {
            LeaveForm newLeaveForm = EntityUtil.toLeaveForm(leaveFormDto);
            LeaveForm existingLeaveForm = existingEmployee.getLeaveForms().stream()
                    .filter(leaveForm -> leaveForm.getId() == newLeaveForm.getId())
                    .findFirst()
                    .orElse(newLeaveForm);

            if (existingLeaveForm.getEmployee() == null) {
                existingLeaveForm.setEmployee(existingEmployee);
            }

            updatedLeaveForms.add(existingLeaveForm);

        }
        //existingEmployee.setLeaveForms(updatedLeaveForms);
       existingEmployee.getLeaveForms().clear();

        for (LeaveForm leaveForm : updatedLeaveForms) {
            existingEmployee.getLeaveForms().add(leaveForm);
        }

        employeeDao.save(existingEmployee);
        return existingEmployee;
    }

    private static void updateNewEmployeeValue(EmployeeLeaveFormDto employeeLeaveFormDto, Employee existingEmployee) {
        existingEmployee.setId(employeeLeaveFormDto.getEmployeeId());
        existingEmployee.setName(employeeLeaveFormDto.getName());
        existingEmployee.setPhoneNumber(employeeLeaveFormDto.getPhoneNumber());
        existingEmployee.setEmail(employeeLeaveFormDto.getEmail());
        existingEmployee.setNrc(employeeLeaveFormDto.getNrc());
        existingEmployee.setDateOfBirth(employeeLeaveFormDto.getDateOfBirth());
        existingEmployee.setGender(employeeLeaveFormDto.getGender());
        existingEmployee.setAddress(employeeLeaveFormDto.getAddress());
    }

    public EmployeeLeaveFormDto addLeaveFormToExistingEmployeee(int employeeId, LeaveFormDto leaveFormDto) {
        Optional<Employee> existingEmployee = employeeDao.findById(employeeId);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            LeaveFormDto newLeaveFormDto = new LeaveFormDto();
            newLeaveFormDto.setEmployeeId(employeeId);
            newLeaveFormDto.setId(leaveFormDto.getId());
            newLeaveFormDto.setFromDate(leaveFormDto.getFromDate());
            newLeaveFormDto.setToDate(leaveFormDto.getToDate());
            newLeaveFormDto.setLeaveType(leaveFormDto.getLeaveType());

            employee.addLeaveForm(EntityUtil.toLeaveForm(newLeaveFormDto));
            Employee savedEmployee = employeeDao.save(employee);
            return EntityUtil.toEmployeeLeaveFormDto(employee);

        }
        throw new EntityNotFoundException("Employee Id Not Found");
    }


}
