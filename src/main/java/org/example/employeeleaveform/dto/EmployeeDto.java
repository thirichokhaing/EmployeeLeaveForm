package org.example.employeeleaveform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.employeeleaveform.entity.Gender;
import org.example.employeeleaveform.entity.LeaveForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Integer id;
    private String name;
    private String phoneNumber;
    private String email;
    private String nrc;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;

}
