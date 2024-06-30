/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.like_hero_to_zero;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Kwanou Harlain
 */
@Entity
//@Table(name = "REGISTER_USER")
@Table(name = "REGISTER_USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "password")
})
@NamedQueries({
    @NamedQuery(name = "RegisterUser.findAll", query = "SELECT r FROM RegisterUser r"),
    @NamedQuery(name = "RegisterUser.findById", query = "SELECT r FROM RegisterUser r WHERE r.id = :id"),
    @NamedQuery(name = "RegisterUser.findByFirstname", query = "SELECT r FROM RegisterUser r WHERE r.firstname = :firstname"),
    @NamedQuery(name = "RegisterUser.findByLastname", query = "SELECT r FROM RegisterUser r WHERE r.lastname = :lastname"),
    @NamedQuery(name = "RegisterUser.findByEmail", query = "SELECT r FROM RegisterUser r WHERE r.email = :email"),
    @NamedQuery(name = "RegisterUser.findByPassword", query = "SELECT r FROM RegisterUser r WHERE r.password = :password")})
public class RegisterUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    
    @NotBlank(message = "First name is required")
    @Size(max = 250, message = "First name cannot be longer than 250 characters")
    @Column(name = "FIRSTNAME")
    private String firstname;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 250, message = "Last name cannot be longer than 250 characters")
    @Column(name = "LASTNAME")
    private String lastname;
    
    @NotBlank(message = "Email is required")
    @Size(max = 250, message = "Email cannot be longer than 250 characters")
    @Column(name = "EMAIL")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Column(name = "PASSWORD")
    private String password;
    
    @OneToMany(mappedBy = "userId")
    private Collection<Co2Emissions> co2EmissionsCollection;

    public RegisterUser() {
    }

    public RegisterUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Co2Emissions> getCo2EmissionsCollection() {
        return co2EmissionsCollection;
    }

    public void setCo2EmissionsCollection(Collection<Co2Emissions> co2EmissionsCollection) {
        this.co2EmissionsCollection = co2EmissionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegisterUser)) {
            return false;
        }
        RegisterUser other = (RegisterUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.like_hero_to_zero.RegisterUser[ id=" + id + " ]";
    }
    
}
