package com.coderlee.jdk8features.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeePredicateBySalary implements MyPredicate<Employee> {
    private double salary;
    @Override
    public boolean test(Employee employee) {
        return employee.getSalary() >= salary;
    }
}
