package com.exercise.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "person")
public class Person
{

    @Id
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private String id;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "DOB", nullable = false)
    private Date dob;

    @Column(name = "EMAIL", nullable = true)
    private String email;

    public Person() {
    }

    public Person(String id) {
        this.id = id;
    }

    public Person(String id, String lastName, String firstName, Date dob, String email) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dob = dob;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public Date getDob() { return dob; }

    public void setDob(String dob) throws ParseException { this.dob = new SimpleDateFormat("yyyy-MM-dd").parse(dob); }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", first name='" + firstName + '\'' +
                ", last name=" + lastName +
                '}';
    }
}