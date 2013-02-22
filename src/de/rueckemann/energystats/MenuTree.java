package de.rueckemann.energystats;

import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Tree;

import de.rueckemann.energystats.domain.EnergyMeter;
import de.rueckemann.energystats.mongo.MongoDB;

@SuppressWarnings("serial")
public class MenuTree extends HorizontalLayout  {

	private Tree tree;
	
	HorizontalLayout editBar;

	private Object selectedItem = null;

	public MenuTree() {
	    setSpacing(true);
	
	    // Create the Tree,add to layout
	    tree = new Tree("Hardware Inventory");
	    tree.setDebugId("energyMeterTree");
	    refresh();
	    addComponent(tree);
	    tree.setImmediate(true);
	    
	}
	
	public void refresh() {
		final HierarchicalContainer container = new HierarchicalContainer();

	    String energyMeterItem = "Energy Meter";
	    container.addItem(energyMeterItem);
	    
	    String gas = "Gas";
	    container.addItem(gas);
	    container.setParent(gas, energyMeterItem);
	    String electricity = "Electricity";
	    container.addItem(electricity);
	    container.setParent(electricity, energyMeterItem);
	    String water = "Water";
	    container.addItem(water);
	    container.setParent(water, energyMeterItem);
	
        
	    Collection<EnergyMeter> energyMeter = MongoDB.getEnergyMeter();
	    for (EnergyMeter em : energyMeter) {

	    	container.addItem(em);
		    String parent = energyMeterItem;
		    if(em.getType().equals("Gas")) {
		    	parent = gas;
		    } else if (em.getType().equals("Electricity")) {
		    	parent = electricity;
		    } else if(em.getType().equals("Water")) {
		    	parent = water;
		    }
		    container.setParent(em, parent);
		    container.setChildrenAllowed(em, false);
		}
	    tree.setContainerDataSource(container);
	    tree.expandItemsRecursively(energyMeterItem);
	    System.out.println("Trying to select " + selectedItem);
	    tree.select(selectedItem);
	}

	public Tree getTree() {
		return tree;
	}

	public void setSelectedItem(Object selected) {
		if(selected != null) {
			this.selectedItem  = selected;
		}
	}


}
