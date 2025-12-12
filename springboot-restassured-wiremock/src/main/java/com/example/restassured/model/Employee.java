package com.example.restassured.model;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Employee {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "first-name")
    private String firstName;

    @XmlElement(name = "last-name")
    private String lastName;

    @XmlElement(name = "sex")
    private String sex;
}
