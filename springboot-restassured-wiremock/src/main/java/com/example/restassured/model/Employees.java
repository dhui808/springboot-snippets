package com.example.restassured.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Employees {

    @XmlElement(name = "employee")
    private List<Employee> employees = new ArrayList<>();
}
