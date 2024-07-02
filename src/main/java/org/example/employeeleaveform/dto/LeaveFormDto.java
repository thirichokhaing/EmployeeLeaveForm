package org.example.employeeleaveform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.employeeleaveform.entity.LeaveType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveFormDto {
    private Integer id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LeaveType leaveType;
    private int employeeId;
}
