package com.therame.model;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private long id;

    @Column(name="role")
    private String role;

    public long getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
