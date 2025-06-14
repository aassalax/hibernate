package com.learning.entity;

import com.learning.utils.AuditTrailListener;
import jakarta.persistence.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@EntityListeners(AuditTrailListener.class)
@Entity
public class User {
    private static Log log = LogFactory.getLog(User.class);

    @Id
    @GeneratedValue
    private int id;

    private String userName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Transient
    private String fullName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @PrePersist
    public void logNewUserAttempt() {
        log.info("Attempting to add new user with username: " + userName);
    }

    @PostPersist
    public void logNewUserAdded() {
        log.info("Added user '" + userName + "' with ID: " + id);
    }

    @PreRemove
    public void logUserRemovalAttempt() {
        log.info("Attempting to delete user: " + userName);
    }

    @PostRemove
    public void logUserRemoval() {
        log.info("Deleted user: " + userName);
    }

    @PreUpdate
    public void logUserUpdateAttempt() {
        log.info("Attempting to update user: " + userName);
    }

    @PostUpdate
    public void logUserUpdate() {
        log.info("Updated user: " + userName);
    }

    @PostLoad
    public void logUserLoad() {
        setFullName(firstName + " " + lastName);
        log.info("PostLoad triggered, fullName set to: " + fullName);
    }
}
