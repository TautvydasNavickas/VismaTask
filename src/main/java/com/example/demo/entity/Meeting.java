package com.example.demo.entity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Map;

@Entity
public class Meeting {

    private String name;
    private String responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<LocalDate, String> attendees;

    public Meeting(String name, String responsiblePerson, String description, Category category, Type type, LocalDate startDate, LocalDate endDate, Map<LocalDate, String> attendees) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = attendees;
    }

    public String getName() {
        return name;
    }

    public Map<LocalDate, String> getAttendees() {
        return attendees;
    }

    public void setAttendees(Map<LocalDate, String> attendees) {
        this.attendees = attendees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
