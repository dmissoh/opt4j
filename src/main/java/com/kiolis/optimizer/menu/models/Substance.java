package com.kiolis.optimizer.menu.models;

public class Substance {

  protected final int id;
  protected final Type type;
  protected final double fruits;//360g per day
  protected final double dairy;//250g per day
  protected final double starches;//400g per day

  public Substance(int id, double fruits, double dairy, double starches, Type type) {
	this.id = id;

	this.fruits = fruits;
	this.dairy = dairy;
	this.starches = starches;

	this.type = type;
  }

  public int getId() {
	return id;
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

  public Type getType() {
	return type;
  }

  @Override
  public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Substance)) return false;

	Substance substance = (Substance) o;

	if (id != substance.id) return false;

	return true;
  }

  @Override
  public int hashCode() {
	return id;
  }

  @Override
  public String toString() {
	return "Substance{" +
			"id=" + id +
			", type=" + type +
			'}';
  }

    /*
	@Override
    public String toString() {
        return "Substance{" +
                "id=" + id +
                ", type=" + type +
                ", fruits=" + fruits +
                ", dairy=" + dairy +
                ", starches=" + starches +
                '}';
    }
    */

  public enum Type {
	STARTER, MAIN, DESSERT
  }
}