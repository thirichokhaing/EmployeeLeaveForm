package org.example.employeeleaveform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.employeeleaveform.dto.EmployeeDto;
import org.example.employeeleaveform.entity.Gender;
import org.example.employeeleaveform.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;


    @RequestMapping({"/", "/all-employees"})
    public String showAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAllEmployees());
        return "employeeList";
    }

    @GetMapping("/form-search")
    public String findEmployeesByName(@RequestParam("nameOrEmail") String nameOrEmail, Model model) {
        if (nameOrEmail == null || nameOrEmail.isEmpty()) {
            model.addAttribute("message", "You need find by email or name,Please enter keyword!!");
        }else {
            model.addAttribute("employees",
                    employeeService.findEmployeeFormByNameOrEmail(nameOrEmail));
        }

        return "employeeSearch";
    }

    @GetMapping("/employee-form")
    public String createEmployee(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        model.addAttribute("genders", Gender.values());
        return "employeeForm";
    }

    @PostMapping("/save-employee")
    public String saveEmployee(EmployeeDto employeeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employeeForm";
        }
        employeeService.createEmployee(employeeDto);
        return "redirect:/employee/all-employees";
    }

    @GetMapping("/delete-employee")
    public String deleteEmployee(@RequestParam("id") int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return "redirect:/employee/all-employees";
    }

    @GetMapping("/find-by-id")
    public String findEmployeeById(@RequestParam("id") int employeeId, Model model) {
        model.addAttribute("employee", employeeService.findEmployeeById(employeeId));
        return "employeeDetails";
    }

    @GetMapping("/form-details")
    public String formDetails(@RequestParam("employeeId") int employeeId, Model model) {
        model.addAttribute("employee", employeeService.findEmployeeById(employeeId));
        return "employeeLeaveFormDetails";
    }

    @GetMapping("/all-forms")
    public String findAllLeaveForms(Model model) {

        model.addAttribute("formList", employeeService.findAllLeaveForms());
        return "leaveFormList";
    }

}
