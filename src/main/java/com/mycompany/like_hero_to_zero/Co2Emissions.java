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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Kwanou Harlain
 */
@Entity
@Table(name = "CO2_EMISSIONS")
@NamedQueries({
    @NamedQuery(name = "Co2Emissions.findAll", query = "SELECT c FROM Co2Emissions c"),
    @NamedQuery(name = "Co2Emissions.findBySeriesName", query = "SELECT c FROM Co2Emissions c WHERE c.seriesName = :seriesName"),
    @NamedQuery(name = "Co2Emissions.findBySeriesCode", query = "SELECT c FROM Co2Emissions c WHERE c.seriesCode = :seriesCode"),
    @NamedQuery(name = "Co2Emissions.findByCountryName", query = "SELECT c FROM Co2Emissions c WHERE c.countryName = :countryName"),
    @NamedQuery(name = "Co2Emissions.findByCountryCode", query = "SELECT c FROM Co2Emissions c WHERE c.countryCode = :countryCode"),
    @NamedQuery(name = "Co2Emissions.findByYr2018", query = "SELECT c FROM Co2Emissions c WHERE c.yr2018 = :yr2018"),
    @NamedQuery(name = "Co2Emissions.findByYr2019", query = "SELECT c FROM Co2Emissions c WHERE c.yr2019 = :yr2019"),
    @NamedQuery(name = "Co2Emissions.findByYr2020", query = "SELECT c FROM Co2Emissions c WHERE c.yr2020 = :yr2020"),
    @NamedQuery(name = "Co2Emissions.findByYr2021", query = "SELECT c FROM Co2Emissions c WHERE c.yr2021 = :yr2021"),
    @NamedQuery(name = "Co2Emissions.findByYr2022", query = "SELECT c FROM Co2Emissions c WHERE c.yr2022 = :yr2022"),
    @NamedQuery(name = "Co2Emissions.findByYr2023", query = "SELECT c FROM Co2Emissions c WHERE c.yr2023 = :yr2023"),
    @NamedQuery(name = "Co2Emissions.findById", query = "SELECT c FROM Co2Emissions c WHERE c.id = :id"),
    @NamedQuery(name = "Co2Emissions.findByAuthor", query = "SELECT c FROM Co2Emissions c WHERE c.author = :author")})
public class Co2Emissions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "SERIES_NAME")
    private String seriesName;
    @Column(name = "SERIES_CODE")
    private String seriesCode;
    @Column(name = "COUNTRY_NAME")
    private String countryName;
    @Column(name = "COUNTRY_CODE")
    private String countryCode;
    @Column(name = "YR2018")
    private String yr2018;
    @Column(name = "YR2019")
    private String yr2019;
    @Column(name = "YR2020")
    private String yr2020;
    @Column(name = "YR2021")
    private String yr2021;
    @Column(name = "YR2022")
    private String yr2022;
    @Column(name = "YR2023")
    private String yr2023;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "AUTHOR")
    private String author;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private RegisterUser userId;

    public Co2Emissions() {
    }

    public Co2Emissions(Integer id) {
        this.id = id;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesCode() {
        return seriesCode;
    }

    public void setSeriesCode(String seriesCode) {
        this.seriesCode = seriesCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getYr2018() {
        return yr2018;
    }

    public void setYr2018(String yr2018) {
        this.yr2018 = yr2018;
    }

    public String getYr2019() {
        return yr2019;
    }

    public void setYr2019(String yr2019) {
        this.yr2019 = yr2019;
    }

    public String getYr2020() {
        return yr2020;
    }

    public void setYr2020(String yr2020) {
        this.yr2020 = yr2020;
    }

    public String getYr2021() {
        return yr2021;
    }

    public void setYr2021(String yr2021) {
        this.yr2021 = yr2021;
    }

    public String getYr2022() {
        return yr2022;
    }

    public void setYr2022(String yr2022) {
        this.yr2022 = yr2022;
    }

    public String getYr2023() {
        return yr2023;
    }

    public void setYr2023(String yr2023) {
        this.yr2023 = yr2023;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public RegisterUser getUserId() {
        return userId;
    }

    public void setUserId(RegisterUser userId) {
        this.userId = userId;
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
        if (!(object instanceof Co2Emissions)) {
            return false;
        }
        Co2Emissions other = (Co2Emissions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.like_hero_to_zero.Co2Emissions[ id=" + id + " ]";
    }
    
}
