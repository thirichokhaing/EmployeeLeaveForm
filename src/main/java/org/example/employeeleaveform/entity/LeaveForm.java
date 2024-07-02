package org.example.employeeleaveform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate toDate;
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    public LeaveForm(LocalDate fromDate, LocalDate toDate, LeaveType leaveType) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveType = leaveType;
    }

    @ManyToOne
    private Employee employee;
}
