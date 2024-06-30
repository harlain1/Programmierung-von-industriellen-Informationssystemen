/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mycompany.like_hero_to_zero;

import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;

/**
 *
 * @author Kwanou Harlain
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

   private String email;
    private String password;

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

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

    public String login() {
        TypedQuery<RegisterUser> query = em.createQuery("SELECT u FROM RegisterUser u WHERE u.email = :email AND u.password = :password", RegisterUser.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        try {
            RegisterUser user = query.getSingleResult();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
            return "List";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", "Login failed"));
            return "Login";
        }
    }
    
}
