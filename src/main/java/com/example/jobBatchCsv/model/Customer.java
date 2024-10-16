package com.example.jobBatchCsv.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CUSTOMER_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "customerId")
    private Long id;

    @Column(name = "customerFname")
    private String firstName;
    @Column(name = "customerLname")
    private String lastName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "contactNo")
    private String contactNo;
    @Column(name = "location")
    private String location;
    @Column(name = "dob")
    private String dob;
    @Column(name = "salary")
    private Double salary;
}
