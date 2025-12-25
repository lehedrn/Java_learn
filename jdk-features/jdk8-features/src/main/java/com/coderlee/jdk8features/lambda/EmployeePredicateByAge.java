package com.coderlee.jdk8features.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeePredicateByAge implements MyPredicate<Employee> {
    private int age;
    @Override
    public boolean test(Employee employee) {
        return employee.getAge() > age;
    }
}
