package com.mrrobot.currencyapp.model;

public class Currency {

    private String ID;
    private String NumCode;
    private String CharCode;
    private Integer Nominal;
    private String Name;
    private Double Value;
    private Double Previous;

    public Currency(String ID, String numCode, String charCode, Integer nominal, String name, Double value, Double previous) {
        this.ID = ID;
        NumCode = numCode;
        CharCode = charCode;
        Nominal = nominal;
        Name = name;
        Value = value;
        Previous = previous;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNumCode() {
        return NumCode;
    }

    public void setNumCode(String numCode) {
        NumCode = numCode;
    }

    public String getCharCode() {
        return CharCode;
    }

    public void setCharCode(String charCode) {
        CharCode = charCode;
    }

    public Integer getNominal() {
        return Nominal;
    }

    public void setNominal(Integer nominal) {
        Nominal = nominal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double value) {
        Value = value;
    }

    public Double getPrevious() {
        return Previous;
    }

    public void setPrevious(Double previous) {
        Previous = previous;
    }
}
