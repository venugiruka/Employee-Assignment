package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.model.Employee;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    @RequestMapping("/v1/bfs/employees/{id}")
    public ResponseEntity<Employee> employeeGetById(String id) throws Exception{

        Employee employee = employeeRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        return ResponseEntity.ok().body(employee);
    }

    @SneakyThrows
    @PostMapping(value = "/v1/bfs/employees")
    @Override
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
    	List<Employee> employeeList = employeeRepository.findByFirstNameAndLastNameAndDateOfBirth(employee.getFirstName(), employee.getLastName(), employee.getDateOfBirth());
    	if(!CollectionUtils.isEmpty(employeeList)) {
    		throw new Exception("Duplicate Employee");
    	}
    	
        return ResponseEntity.ok().body(employeeRepository.save(employee));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String mssg) {
            super(mssg);
        }
    }


}
