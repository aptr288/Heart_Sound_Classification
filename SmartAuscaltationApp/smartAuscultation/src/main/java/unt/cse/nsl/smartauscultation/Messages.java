package unt.cse.nsl.smartauscultation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "unt.cse.nsl.smartauscultation.messages"; //$NON-NLS-1$

	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private static final String LOG_NAME = "unt.cse.nsl.smartauscultation.logmessages"; //$NON-NLS-1$

	public static final ResourceBundle LOG_BUNDLE = ResourceBundle
			.getBundle(LOG_NAME);
	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			try {
				return LOG_BUNDLE.getString(key);
			} catch (MissingResourceException eX) {
				return '!' + key + '!';
			}
		}
	}
}
