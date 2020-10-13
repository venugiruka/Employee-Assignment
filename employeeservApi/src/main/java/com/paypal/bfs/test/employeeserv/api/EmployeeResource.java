package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id) throws Exception;

    // ----------------------------------------------------------
    // TODO - add a new operation for creating employee resource.
    // ----------------------------------------------------------
    ResponseEntity<Employee> createEmployee(Employee employee);

}
