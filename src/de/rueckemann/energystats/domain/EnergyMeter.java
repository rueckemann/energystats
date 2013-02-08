package de.rueckemann.energystats.domain;

import java.io.Serializable;
import java.util.Date;

public class EnergyMeter extends DomainObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private String label;
    private String type;    
    private String location;
    private String manufacturer; 
   

    public EnergyMeter() {
     }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String toString() {
		return label;
	}
}