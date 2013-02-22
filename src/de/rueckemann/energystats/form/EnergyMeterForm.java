package de.rueckemann.energystats.form;

	import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.rueckemann.energystats.FormUtil;
import de.rueckemann.energystats.domain.EnergyMeter;
import de.rueckemann.energystats.mongo.MongoDB;

	@SuppressWarnings("serial")
	public class EnergyMeterForm extends VerticalLayout {

		private List<ChangeListener> listeners = Collections.synchronizedList(new ArrayList<ChangeListener>());

		final Form energyMeterForm;

	    
	    public EnergyMeterForm() {
	    	
	    	// Create the Form
	    	energyMeterForm = new Form();

	        BeanItem<EnergyMeter> energyMeterItem = new BeanItem<EnergyMeter>(new EnergyMeter()); 

	        energyMeterForm.setCaption("Energy Meter");
	        energyMeterForm.setWriteThrough(false); // we want explicit 'apply'
	        energyMeterForm.setInvalidCommitted(false); // no invalid values in datamodel

	        // FieldFactory for customizing the fields and adding validators
	        energyMeterForm.setFormFieldFactory(new EnergyMeterFieldFactory());
	        energyMeterForm.setItemDataSource(energyMeterItem); // bind to POJO via BeanItem

	        // Determines which properties are shown, and in which order:
	        energyMeterForm.setVisibleItemProperties(Arrays.asList(new String[] {
	                "label", "type", "location",
	                "manufacturer" }));

	        // Add form to layout
	        addComponent(energyMeterForm);

	        // The cancel / apply buttons
	        HorizontalLayout buttons = new HorizontalLayout();
	        buttons.setSpacing(true);
	        Button discardChanges = new Button("Cancel",
	                new Button.ClickListener() {
	                    public void buttonClick(ClickEvent event) {
	                        energyMeterForm.discard();
	                    }
	                });
	        discardChanges.setDebugId("btDiscardChanges");
	        buttons.addComponent(discardChanges);
	        buttons.setComponentAlignment(discardChanges, Alignment.MIDDLE_LEFT);

	        Button apply = new Button("Apply", new Button.ClickListener() {
	            @SuppressWarnings("unchecked")
				public void buttonClick(ClickEvent event) {
	                try {
	                    energyMeterForm.commit();
	                    EnergyMeter bean = ((BeanItem<EnergyMeter>)energyMeterForm.getItemDataSource()).getBean();
	                    int updateCount = MongoDB.saveObject(bean);
	                    FormUtil.showMessage(getWindow(), "Update successful", "Updated " + updateCount + " elements.");
	                    notifyListeners(bean);
	                } catch (Exception e) {
	                    // Ignored, we'll let the Form handle the errors
	                }
	            }
	        });
	        apply.setDebugId("btApply");
	        buttons.addComponent(apply);
	        
	        
	        Button remove = new Button("Remove", new Button.ClickListener() {
	            @SuppressWarnings("unchecked")
				public void buttonClick(ClickEvent event) {
	                try {
	                    EnergyMeter bean = ((BeanItem<EnergyMeter>)energyMeterForm.getItemDataSource()).getBean();
	                    int updateCount = MongoDB.removeObject(bean);
	                    FormUtil.showMessage(getWindow(), "Update successful", "Updated " + updateCount + " elements.");
	                    notifyListeners(bean);
	                } catch (Exception e) {
	                	FormUtil.showMessage(getWindow(), "Error", e.getMessage());
	                }
	            }
	        });
	        remove.setDebugId("btRemove");
	        buttons.addComponent(remove);

	        energyMeterForm.getFooter().addComponent(buttons);
	        energyMeterForm.getFooter().setMargin(false, false, true, true);

	        // button for showing the internal state of the POJO
	        Button showPojoState = new Button("Show POJO internal state",
	                new Button.ClickListener() {
	                    public void buttonClick(ClickEvent event) {
	                        showPojoState();
	                    }
	                });
	        addComponent(showPojoState);
	    }

	    private void showPojoState() {
	        Window.Notification n = new Window.Notification("POJO state",
	                Window.Notification.TYPE_TRAY_NOTIFICATION);
	        n.setPosition(Window.Notification.POSITION_CENTERED);
	        @SuppressWarnings("unchecked")
			EnergyMeter energyMeter = ((BeanItem<EnergyMeter>)energyMeterForm.getItemDataSource()).getBean();
            
	        n.setDescription("Identifier: " + energyMeter.getLabel()
	                + "<br/>Type: " + energyMeter.getType() + "<br/>Country: "
	                + "<br/>Location: " + energyMeter.getLocation()  
	                + "<br/>Manufacturer: " + energyMeter.getManufacturer()  
	                + "<br/>UUID: " + energyMeter.get_id());
	        getWindow().showNotification(n);
	    }

	    public Form getEnergyMeterForm() {
	    	return energyMeterForm;
	    }

		public void addChangeListener(ChangeListener listener) {
			this.listeners.add(listener);
		}
		
		public void removeChangeListener(ChangeListener listener) {
			this.listeners.remove(listener);
		}

		private void notifyListeners(EnergyMeter em) {
			for (ChangeListener listener  : listeners) {
				listener.stateChanged(new ChangeEvent(em) );
			}
		}
	}
