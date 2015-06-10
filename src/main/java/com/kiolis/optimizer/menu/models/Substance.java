package com.kiolis.optimizer.menu.models;

import java.io.Serializable;

public class Substance implements Serializable {

  protected final String id;

  protected String label;

  protected Type type;

  protected double fruits;

  protected double dairy;

  protected double starches;

  protected double vegetables;

  protected double meatFishEggs;

  public Substance(String id) {
	this.id = id;
  }

  public String getId() {
	return id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public double getFruits() {
	return fruits;
  }

  public double getDairy() {
	return dairy;
  }

  public double getStarches() {
	return starches;
  }

  public double getVegetables() {
    return vegetables;
  }

  public double getMeatFishEggs() {
    return meatFishEggs;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setFruits(double fruits) {
    this.fruits = fruits;
  }

  public void setDairy(double dairy) {
    this.dairy = dairy;
  }

  public void setStarches(double starches) {
    this.starches = starches;
  }

  public void setVegetables(double vegetables) {
    this.vegetables = vegetables;
  }

  public void setMeatFishEggs(double meatFishEggs) {
    this.meatFishEggs = meatFishEggs;
  }

  public Type getType() {
	return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Substance substance = (Substance) o;

    return id.equals(substance.id);

  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Substance{" +
            "label='" + label + '\'' +
            ", type=" + type +
            '}';
  }

  public enum Type {

    STARTER("CUSTOM_131"),
    MAIN("CUSTOM_132"),
    DESSERT("CUSTOM_134");

    private String code;

    Type(String code){
      this.code = code;
    }

    public String getCode(){
      return code;
    }
  }
}