package com.kp.eventchey.domain;

import java.util.Objects;

public class Attendee {
    private String id;
    private String email;
    private String phone;
    private String name;
    private AttendeeStatus status;

    public Attendee() {
    }

    public Attendee(String id, String email, String phone, String name, AttendeeStatus status) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttendeeStatus getStatus() {
        return status;
    }

    public void setStatus(AttendeeStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendee attendee = (Attendee) o;
        return Objects.equals(id, attendee.id) &&
                Objects.equals(email, attendee.email) &&
                Objects.equals(phone, attendee.phone) &&
                Objects.equals(name, attendee.name) &&
                status == attendee.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, name, status);
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}

