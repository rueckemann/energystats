package de.rueckemann.energystats;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

import de.rueckemann.energystats.domain.EnergyMeter;
import de.rueckemann.energystats.form.EnergyMeterForm;

@SuppressWarnings("serial")
public class EnergystatsApplication extends Application {
	@Override
	public void init() {
		final Window mainWindow = new Window("Energystats Application");
		
		VerticalLayout mainLayout= new VerticalLayout();
		mainLayout.setSizeFull();
		mainWindow.setContent(mainLayout);
				
		
		final VerticalSplitPanel vert = new VerticalSplitPanel();
        vert.setHeight("550px");
        vert.setWidth("100%");
        vert.setSplitPosition(50, Sizeable.UNITS_PIXELS);
       
        mainLayout.addComponent(vert);

        Label lbTitle = new Label("Know how much energy you spend");
		lbTitle.setDebugId("lbTitle");
		vert.addComponent(lbTitle);

		// Add a horizontal SplitPanel to the lower area
        final HorizontalSplitPanel horiz = new HorizontalSplitPanel();
        horiz.setSplitPosition(20); // percent
        vert.addComponent(horiz);

        // left component:
        final MenuTree menuTree = new MenuTree();
        VerticalLayout left = new VerticalLayout();
        Button addNew = new Button("Add Item");
        Button refresh = new Button("Refresh");
        addNew.setStyleName(BaseTheme.BUTTON_LINK);
        left.addComponent(addNew);
        left.addComponent(refresh);
        left.addComponent(menuTree);
        horiz.addComponent(left);

        // right component:
	    final EnergyMeterForm energyMeterForm = new EnergyMeterForm();
	    energyMeterForm.setVisible(false);
        horiz.addComponent(energyMeterForm);

        // Lock toggle button
        CheckBox toggleLocked = new CheckBox("Splits locked",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        vert.setLocked(event.getButton().booleanValue());
                        horiz.setLocked(event.getButton().booleanValue());
                    }
                });
        toggleLocked.setImmediate(true);
        mainLayout.addComponent(toggleLocked);

	    menuTree.getTree().addListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object selected = menuTree.getTree().getValue();
				
				if(selected instanceof EnergyMeter) {
					BeanItem<EnergyMeter> energyMeterItem = new BeanItem<EnergyMeter>((EnergyMeter)selected);
					energyMeterForm.getEnergyMeterForm().setItemDataSource(energyMeterItem);
					energyMeterForm.setVisible(true);
					menuTree.setSelectedItem(selected);
				} else {
					energyMeterForm.setVisible(false);
				}
			}
		});
	    
	    energyMeterForm.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				menuTree.refresh();
			} 
	    });

        addNew.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
            	BeanItem<EnergyMeter> energyMeterItem = new BeanItem<EnergyMeter>(new EnergyMeter());
				energyMeterForm.getEnergyMeterForm().setItemDataSource(energyMeterItem);
				energyMeterForm.setVisible(true);
            }
        });
        
        refresh.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
            	menuTree.refresh();
            }
        });
        
		setMainWindow(mainWindow);
	}

}
