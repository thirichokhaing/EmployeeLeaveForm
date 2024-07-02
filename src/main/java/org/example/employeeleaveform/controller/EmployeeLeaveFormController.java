package org.example.employeeleaveform.controller;

import lombok.RequiredArgsConstructor;
import org.example.employeeleaveform.dto.EmployeeLeaveFormDto;
import org.example.employeeleaveform.dto.LeaveFormDto;
import org.example.employeeleaveform.entity.Gender;
import org.example.employeeleaveform.entity.LeaveType;
import org.example.employeeleaveform.service.EmployeeLeaveFormService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EmployeeLeaveFormController {
    private final EmployeeLeaveFormService employeeLeaveFormService;

    @RequestMapping({"/","/create-form"})
    public String createLeaveForm(Model model) {
        model.addAttribute("employeeLeaveForm", new EmployeeLeaveFormDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("leaveTypes", LeaveType.values());
        return "leaveForm";
    }

    @PostMapping("/save-form")
    public String saveLeaveForm(EmployeeLeaveFormDto employeeLeaveFormDto, BindingResult result) {
        if (result.hasErrors()) {
            return "leaveForm";
        }
        EmployeeLeaveFormDto dto = employeeLeaveFormService.createEmployeeLeaveForm(employeeLeaveFormDto);
        return "redirect:/form-details?id=" + dto.getEmployeeId();

    }

    @RequestMapping("/form-details")
    public String leaveFormDetails(@RequestParam int id, Model model) {
        model.addAttribute("formDetails", employeeLeaveFormService.findEmployeeLeaveFormById(id));
        return "leaveFormDetail";
    }


    @GetMapping("/delete-form")
    public String deleteLeaveForm(@RequestParam("employeeId") int empId) {
        employeeLeaveFormService.deleteLeaveForm(empId);
        return "leaveFormDelete";
    }

    int empId;

    @GetMapping("/update-form")
    public String updateForm(@RequestParam int id, Model model) {
        model.addAttribute("updateForm", employeeLeaveFormService.findEmployeeLeaveFormById(id));
        model.addAttribute("genders", Gender.values());
        model.addAttribute("leaveTypes", LeaveType.values());
        this.empId = id;
        return "leaveFormUpdate";
    }

    @PostMapping("/update-form")
    public String processForm(@ModelAttribute("updateForm") EmployeeLeaveFormDto employeeLeaveFormDto, BindingResult result) {
        if (result.hasErrors()) {
            return "leaveFormUpdate";
        }
        employeeLeaveFormDto.setEmployeeId(empId);
        EmployeeLeaveFormDto dto = employeeLeaveFormService.updateForm(employeeLeaveFormDto, empId);
        return "redirect:/form-details?id=" + dto.getEmployeeId();
    }

    @GetMapping("/add-new-form")
    public String addNewForm(@RequestParam("empId") int id,Model model) {
        model.addAttribute("form",new LeaveFormDto());
        model.addAttribute("leaveTypes", LeaveType.values());
        this.empId = id;
        return "newLeaveForm";
    }

    @PostMapping("/add-new-form")
    public String addingProcess(LeaveFormDto leaveFormDto,BindingResult result) {
        if (result.hasErrors()) {
            return "newLeaveForm";
        }
        leaveFormDto.setEmployeeId(empId);
        EmployeeLeaveFormDto employeeLeaveFormDto = employeeLeaveFormService.addLeaveFormToExistingEmployeee(empId,leaveFormDto);
        return "redirect:/form-details?id=" + employeeLeaveFormDto.getEmployeeId();
    }


}
