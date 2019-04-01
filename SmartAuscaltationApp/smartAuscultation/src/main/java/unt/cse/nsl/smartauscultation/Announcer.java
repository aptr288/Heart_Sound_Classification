package unt.cse.nsl.smartauscultation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

/**
 * This class provides some useful methods for detecting voice accessibility and
 * announcing content to accessibility services.
 * 
 * @author logan
 * 
 */
public class Announcer {

	/**
	 * Announces the contentDescription of a {@link View} to the user.
	 * 
	 * @param view
	 */
	public static synchronized void makeAnnouncement(View view) {

		AccessibilityManager am = (AccessibilityManager) view.getContext()
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
		if (am.isEnabled()) {
			Log.i("Announcer", "Sending a11y event for text: "
					+ view.getContentDescription().toString());
//			view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_EXIT);
//			view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
//			view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_EXIT);
		}
	}

	/**
	 * Checks whether some kind of voice accessibility feature is enabled.
	 * 
	 * @param context
	 *            The {@link Context} to use to get the
	 *            {@link AccessibilityManager}
	 * @return
	 */
	public static boolean isVoiceA11yEnabled(Context context) {
		AccessibilityManager am = (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
//		List<AccessibilityServiceInfo> svcAudList = am
//				.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_AUDIBLE);
//		List<AccessibilityServiceInfo> svcSpokList = am
//				.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
//		if (svcAudList != null && !svcAudList.isEmpty()) {
//			Log.i("Announcer", "Audio feedback list is not empty");
//			return true;
//		} else if (svcSpokList != null && !svcSpokList.isEmpty()) {
//			Log.i("Announcer", "Spoken feedback list is not empty");
//			return true;
//		} else {
//			Log.i("Announcer", "Feedback lists are empty");
			return false;
//		}
	}
}
