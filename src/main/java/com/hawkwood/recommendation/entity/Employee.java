package com.hawkwood.recommendation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="employee")
public class Employee {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
@Column(name="id")
private Integer Id;
@Column(name="first_name")
private String firstName;
@Column(name="last_name")
private String lastName;
@Column(name="email")
private String email;
public int getId() {
	return Id;
}
public void setId(int id) {
	Id = id;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}
@Override
public String toString() {
	return "employee [Id=" + Id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
}


}
