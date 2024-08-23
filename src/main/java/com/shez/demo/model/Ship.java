package com.shez.demo.model;

/**
 * Data model class for a ship.
 */
public class Ship {
    private long id = 0;
    private String model;
    private String description;
    private boolean flown = false;

    public Ship() {}

    public Ship(String model, String descr, boolean hasFlown) {
        this.model = model;
        this.description = descr;
        this.flown = hasFlown;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFlown() {
        return flown;
    }

    public void setFlown(boolean flown) {
        this.flown = flown;
    }

    @Override
    public String toString() {
        return String.format("Ship [id=%d, model=%s, descr=%s, hasFlown=%b]", id, model, description, flown);
    }
}
