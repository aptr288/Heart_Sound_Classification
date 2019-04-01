package unt.cse.nsl.smartauscultation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import org.unt.cse.nsl.util.Timer;

import unt.cse.nsl.smartauscultation.PreferenceDatabase.User;

public class Splash extends Activity {

	public static final int SPLASH_TIME_MS = 1200;

	static Splash self;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Get the handle for this instance and keep it
		self = this;

		new LoadFilesTask().execute();

		// Check for accessibility issues
		checkAccessibility();


	}

	/**
	 * After the audio a11y check is done, this will actually start the application. 
	 */
	private void resumeStartup() {
		// After a delay, start the user selection.
		new Thread(new Runnable() {

			@Override
			public void run() {
				long start = Timer.getMillis();
				PreferenceDatabase.open(self);
				long end = Timer.getMillis();
				long sleepTime = (SPLASH_TIME_MS - (end - start));
				if (sleepTime > 50) {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						runUserSelect();
					}
				});
			}
		}).start();
	}

	/**
	 * Audio-based a11y settings will try to route audio through the
	 * microphone port unless configured otherwise. 
	 * This will cause interference if the user uses the stethoscope. 
	 * Thus, the code segment below detects this and notifies the user to take the appropriate action before continuing. 
	 */
	private void checkAccessibility() {
		
		
		if (Announcer.isVoiceA11yEnabled(this)) {
			//make alert
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(Messages
					.getString("Splash.voice_a11y_detected_title"));
			alert.setMessage(Messages
					.getString("Splash.voice_a11y_detected_instructions"));
			alert.setCancelable(false);
			// customize alert dialog to allow desired input
			alert.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							resumeStartup();
						}
					});
			Dialog dialog = alert.create();
			dialog.show();
		} else
			resumeStartup();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!PreferenceDatabase.isOpen())
			PreferenceDatabase.open(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		PreferenceDatabase.close();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void runUserSelect() {
		// Begin building the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Messages.getString("Splash.user_select")); //$NON-NLS-1$
		builder.setCancelable(false);

		// Get all of the available users
		final User[] users = PreferenceDatabase.getUsers();
		if (users == null) {
			Log.e(this.getClass().getSimpleName() + "." //$NON-NLS-1$
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					Messages.getString("Splash.user_db_closed_error")); //$NON-NLS-1$
			return;
		}
		final String[] options = new String[users.length + 2];
		for (int i = 0; i < users.length; i++) {
			options[i] = users[i].name;
		}
		options[users.length] = Messages.getString("Splash.new_user_option"); //$NON-NLS-1$
		options[users.length + 1] = Messages
				.getString("Splash.remove_user_option"); //$NON-NLS-1$

		// Fill the dialog with the user options
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which < users.length) {
					// Old user
					loadAndStartWithUser(users[which]);
				} else if (which == options.length - 2) {
					// New user
					// Start dialog to ask for user name
					runNewUser();
				} else if (which == options.length - 1) {
					// Remove user
					// Start dialog to ask for user name
					runRemoveUser();
				}
			}
		});

		// Create and start the dialog
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void runNewUser() {
		// Begin building the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Messages.getString("Splash.new_user_name_prompt")); //$NON-NLS-1$
		builder.setCancelable(false);
		final EditText nameInput = new EditText(this);
		nameInput.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		builder.setView(nameInput);

		// Set actions
		builder.setPositiveButton(
				Messages.getString("Splash.ok_option"), new DialogInterface.OnClickListener() { //$NON-NLS-1$
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Get the name and create an account
						String name = nameInput.getText().toString();
						User user = PreferenceDatabase.createUser(name);

						// Start using the new user
						loadAndStartWithUser(user);
					}
				});
		builder.setNegativeButton(Messages.getString("Splash.cancel_option"), //$NON-NLS-1$
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Return to the previous dialog for user select
						runUserSelect();
					}
				});

		// Create and start the dialog
		Dialog dialog = builder.create();
		dialog.show();
	}

	private static void loadAndStartWithUser(User user) {
		// Load the preferences
		PreferenceDatabase.loadPreferencesForUser(self, user);

		// Check the preferences to see if we need to run calibration or not
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(self);
		boolean shouldRunFreqFinder = !(prefs
				.contains(PreferenceDatabase.SETTING_NAME_UPPERFREQ));

		if (shouldRunFreqFinder) {
			Intent intent = new Intent(self, SmartAuscultationActivity.class);
			self.startActivity(intent);
		} else {
			Intent intent = new Intent(self, SmartAuscultationActivity.class);
			self.startActivity(intent);
		}
		self.finish();
	}

	private void runRemoveUser() {
		// Begin building the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Messages.getString("Splash.remove_user_option")); //$NON-NLS-1$
		builder.setCancelable(false);

		// Get all of the available users
		final User[] users = PreferenceDatabase.getUsers();
		if (users == null) {
			Log.e(this.getClass().getSimpleName() + "." //$NON-NLS-1$
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					Messages.getString("Splash.user_db_closed_error")); //$NON-NLS-1$
			return;
		}
		final String[] options = new String[users.length];
		for (int i = 0; i < users.length; i++) {
			options[i] = users[i].name;
		}

		// Fill the dialog with the user options
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PreferenceDatabase.removeUser(users[which]);
				// return
				runUserSelect();
			}
		});
		builder.setNegativeButton(
				Messages.getString("Splash.cancel_option"), new DialogInterface.OnClickListener() { //$NON-NLS-1$
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Just return
						runUserSelect();
					}
				});

		// Create and start the dialog
		Dialog dialog = builder.create();
		dialog.show();
	}

    /**
     * load coefficients for CWT
     * load HMM models
     */
	private class LoadFilesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

            // Calling Application class (see application tag in AndroidManifest.xml)
//            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
//            globalVariable.readS2Files();

			return null;
		}
	}
}
