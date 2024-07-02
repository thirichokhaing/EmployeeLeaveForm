package org.example.employeeleaveform.dao;

import org.example.employeeleaveform.entity.LeaveForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LeaveFormDao extends JpaRepository<LeaveForm, Integer> {

    @Query("select l from LeaveForm l where l.employee.id =:employeeId")
    Optional<LeaveForm> findLeaveFormByEmployeeId(@Param("employeeId") int employeeId);

    @Transactional
    @Modifying
    @Query("delete from LeaveForm l where l.employee.id =:employeeId")
    void deleteLeaveFormByEmployeeId(@Param("employeeId") int employeeId);
}
