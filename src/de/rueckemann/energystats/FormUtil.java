package de.rueckemann.energystats;

import com.vaadin.ui.Window;

public class FormUtil {

	public static void showMessage(Window window, String caption, String message) {
		Window.Notification n = new Window.Notification(caption, Window.Notification.TYPE_TRAY_NOTIFICATION);
        n.setPosition(Window.Notification.POSITION_CENTERED);
        n.setDescription(message);
        window.showNotification(n);
	}
}
