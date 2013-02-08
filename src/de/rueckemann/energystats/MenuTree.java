package de.rueckemann.energystats;

import java.util.Collection;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Tree;

import de.rueckemann.energystats.domain.EnergyMeter;
import de.rueckemann.energystats.mongo.MongoDB;

@SuppressWarnings("serial")
public class MenuTree extends HorizontalLayout  {

	private Tree tree;
	
	HorizontalLayout editBar;

	public MenuTree() {
	    setSpacing(true);
	
	    // Create the Tree,add to layout
	    tree = new Tree("Hardware Inventory");
	    addComponent(tree);
	
	    String energyMeterItem = "Energy Meter";
	    tree.addItem(energyMeterItem);
	    
	    String gas = "Gas";
	    tree.addItem(gas);
	    tree.setParent(gas, energyMeterItem);
	    String electricity = "Electricity";
	    tree.addItem(electricity);
	    tree.setParent(electricity, energyMeterItem);
	    String water = "Water";
	    tree.addItem(water);
	    tree.setParent(water, energyMeterItem);
	    
	    
	    Collection<EnergyMeter> energyMeter = MongoDB.getEnergyMeter();
	    for (EnergyMeter em : energyMeter) {
		    tree.addItem(em);
		    String parent = energyMeterItem;
		    if(em.getType().equals("Gas")) {
		    	parent = gas;
		    } else if (em.getType().equals("Electricity")) {
		    	parent = electricity;
		    } else if(em.getType().equals("Water")) {
		    	parent = water;
		    }
		    tree.setParent(em, parent);
		    tree.setChildrenAllowed(em, false);
		    tree.setImmediate(true);
		}
	 }
	
	public Tree getTree() {
		return tree;
	}


}
