package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String Name;
    private String ResponsiblePerson;
    private String Description;
    private Category category;
    private Type type;
    private LocalDate StartDate;
    private LocalDate EndDate;


    public Meeting(String name, String responsiblePerson, String description, Category category, Type type, LocalDate startDate, LocalDate endDate) {
        Name = name;
        ResponsiblePerson = responsiblePerson;
        Description = description;
        this.category = category;
        this.type = type;
        StartDate = startDate;
        EndDate = endDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getResponsiblePerson() {
        return ResponsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        ResponsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }
}
