package unt.cse.nsl.smartauscultation;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Log;

import org.unt.cse.nsl.rls.RecursiveLeastSquareFilter;

/**
 * Interface to interact with the user database.
 * 
 * @author Zachary Morgan
 */
public class PreferenceDatabase {

	public static final String SETTING_NAME_AGE = "age";
	public static final String SETTING_NAME_WEIGHT = "weight";
	public static final String SETTING_NAME_HEIGHT = "height";
	public static final String SETTING_NAME_CONTINUOUS = "continuous";
	public static final String SETTING_NAME_LOWERFREQ = "lowerFreq";
	public static final String SETTING_NAME_UPPERFREQ = "upperFreq";
	public static final String SETTING_NAME_SYSOFFSET = "systolicOffset";
	public static final String SETTING_NAME_DIASOFFSET = "diastolicOffset";
	public static final String SETTING_NAME_REGMATRIX = "regressionMatrix";
	public static final String SETTING_NAME_PMATRIX = "pMatrix";

	/**
	 * Simple helper class to tie an id to a user.
	 * 
	 * @author Zachary Morgan
	 */
	public static class User {
		int id;
		String name;

		public String getName() {
			return name;
		}
	}

	/**
	 * Describes the user table name and column names.
	 * 
	 * @author Zachary Morgan
	 */
	private static abstract class UserTable implements BaseColumns {
		public static final String TABLE_NAME = "users";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_AGE = "age"; // deprecated
		public static final String COLUMN_NAME_WEIGHT = "weight"; // deprecated
		public static final String COLUMN_NAME_HEIGHT = "height"; // deprecated
		public static final String COLUMN_NAME_CONTINUOUS = "continuous";
		public static final String COLUMN_NAME_LOWER_FREQ = "lowerfreq";
		public static final String COLUMN_NAME_UPPER_FREQ = "upperfreq";
		public static final String COLUMN_NAME_SYSTOLIC_OFFSET = "sysoffset";
		public static final String COLUMN_NAME_DIASTOLIC_OFFSET = "diasoffset";
		public static final String COLUMN_NAME_REG_MATRIX = "regmatrix";
		public static final String COLUMN_NAME_P_MATRIX = "pmatrix";

		private UserTable() {
		}
	}

	/**
	 * This class is used to perform upgrades and downgrades on the database. If
	 * any modifications are made to the database, they should be implemented
	 * here and the database version should be incremented below.
	 * 
	 * @author Zachary Morgan
	 */
	private static class DbHelper extends SQLiteOpenHelper {
		public static final int DATABASE_VERSION = 2;
		public static final String DATABASE_NAME = "MyBloodPressure.db";

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + UserTable.TABLE_NAME + " ( "
					+ UserTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
					+ UserTable.COLUMN_NAME_NAME + " TEXT, "
					+ UserTable.COLUMN_NAME_AGE + " REAL, "
					+ UserTable.COLUMN_NAME_WEIGHT + " REAL, "
					+ UserTable.COLUMN_NAME_HEIGHT + " REAL, "
					+ UserTable.COLUMN_NAME_CONTINUOUS + " INTEGER, "
					+ UserTable.COLUMN_NAME_LOWER_FREQ + " INTEGER, "
					+ UserTable.COLUMN_NAME_UPPER_FREQ + " INTEGER, "
					+ UserTable.COLUMN_NAME_SYSTOLIC_OFFSET + " INTEGER, "
					+ UserTable.COLUMN_NAME_DIASTOLIC_OFFSET + " INTEGER, "
					+ UserTable.COLUMN_NAME_REG_MATRIX + " TEXT, "
					+ UserTable.COLUMN_NAME_P_MATRIX + " TEXT " + ")";
			db.execSQL(sql);
			Log.i(PreferenceDatabase.class.getSimpleName(),
					"User table created.");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Handle upgrading the database recursively
			if (newVersion > oldVersion) {
				if (newVersion == oldVersion + 1) {
					// Perform the upgrade
					switch (newVersion) {
					case 2:
						String sql = "DROP TABLE " + UserTable.TABLE_NAME;
						db.execSQL(sql);
						Log.i(PreferenceDatabase.class.getSimpleName(),
								"Dropped old table.");
						onCreate(db);
						break;
					default:
						break;
					}
				} else {
					onUpgrade(db, oldVersion, oldVersion + 1);
					onUpgrade(db, oldVersion + 1, newVersion);
				}
			} else {
				// Downgrade
			}
		}
	}

	// BEGIN DATA
	private static boolean isOpen = false;
	private static DbHelper dbHelper = null;
	private static SQLiteDatabase db = null;
	private static User currentlyLoadedUser = null;

	// END DATA

	/**
	 * Open the database
	 * 
	 * @param context
	 *            The context to open in.
	 * @return Whether the database was successfully opened.
	 */
	public static boolean open(Context context) {
		Log.i(PreferenceDatabase.class.getSimpleName(),
				"Opening preference database.");
		if (!isOpen) {
			dbHelper = new DbHelper(context);
			db = dbHelper.getWritableDatabase();
			isOpen = true;
			return true;
		}
		return true;
	}

	/**
	 * Close the database, if it is open.
	 */
	public static void close() {
		Log.i(PreferenceDatabase.class.getSimpleName(),
				"Closing preference database.");
		if (isOpen) {
			db.close();
			dbHelper.close();
			dbHelper = null;
			db = null;
			isOpen = false;
		}
	}

	/**
	 * Create a new user profile in the database with the given name.
	 * 
	 * @param userName
	 * @return
	 */
	public static User createUser(String userName) {
		if (isOpen) {
			ContentValues values = new ContentValues();
			values.put(UserTable.COLUMN_NAME_NAME, userName);
			long rowId = db.insert(UserTable.TABLE_NAME, null, values);
			if (rowId == -1)
				return null;
			User user = new User();
			user.id = (int) rowId;
			user.name = userName;
			return user;
		}
		Log.e(PreferenceDatabase.class.getSimpleName() + "."
				+ Thread.currentThread().getStackTrace()[2].getMethodName(),
				"The database is not open.");
		return null;
	}

	public static User[] getUsers() {
		if (isOpen) {
			String columns[] = { UserTable.COLUMN_NAME_ID,
					UserTable.COLUMN_NAME_NAME };
			Cursor c = db.query(UserTable.TABLE_NAME, columns, null, null,
					null, null, null);
			User[] users = new User[c.getCount()];
			c.moveToFirst();
			for (int i = 0; i < users.length; i++) {
				users[i] = new User();
				users[i].id = c.getInt(0);
				users[i].name = c.getString(1);
				c.moveToNext();
			}
			c.close();
			return users;
		}
		Log.e(PreferenceDatabase.class.getSimpleName() + "."
				+ Thread.currentThread().getStackTrace()[2].getMethodName(),
				"The database is not open.");
		return null;
	}

	public static void loadPreferencesForUser(Context context, User user) {
		if (user == null) {
			Log.e(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"NULL user!");
			return;
		}
		if (isOpen) {
			Log.i(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"Loading preferences for " + user.name);

			// Get the shared preferences.
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor prefEditor = preferences.edit();
			prefEditor.clear();

			// Get the user's preferences
			String columns[] = { UserTable.COLUMN_NAME_AGE,
					UserTable.COLUMN_NAME_WEIGHT, UserTable.COLUMN_NAME_HEIGHT,
					UserTable.COLUMN_NAME_CONTINUOUS,
					UserTable.COLUMN_NAME_LOWER_FREQ,
					UserTable.COLUMN_NAME_UPPER_FREQ,
					UserTable.COLUMN_NAME_SYSTOLIC_OFFSET,
					UserTable.COLUMN_NAME_DIASTOLIC_OFFSET,
					UserTable.COLUMN_NAME_REG_MATRIX,
					UserTable.COLUMN_NAME_P_MATRIX };
			Cursor c = db.query(UserTable.TABLE_NAME, columns,
					UserTable.COLUMN_NAME_ID + " = ?",
					new String[] { Integer.toString(user.id) }, null, null,
					null);

			// Write the user's preferences into the shared preferences
			if (c.moveToFirst()) {
				if (!c.isNull(0)) {
					int age = (int) c.getFloat(0);
					prefEditor.putString(SETTING_NAME_AGE,
							Integer.toString(age));
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting age to " + age);
				} else {
					// Age not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no age set.");
				}
				if (!c.isNull(1)) {
					int weight = (int) c.getFloat(1);
					prefEditor.putString(SETTING_NAME_WEIGHT,
							Integer.toString(weight));
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting weight to "
							+ weight);
				} else {
					// Weight not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no weight set.");
				}
				if (!c.isNull(2)) {
					int height = (int) c.getFloat(2);
					prefEditor.putString(SETTING_NAME_HEIGHT,
							Integer.toString(height));
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting height to "
							+ height);
				} else {
					// Height not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no height set.");
				}
				if (!c.isNull(3)) {
					boolean continuous = c.getInt(3) != 0 ? true : false;
					prefEditor.putBoolean(SETTING_NAME_CONTINUOUS, continuous);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting continuous to "
							+ continuous);
				} else {
					// Continuous not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has not set continuous.");
				}
				if (!c.isNull(4)) {
					int lowerFreq = c.getInt(4);
					prefEditor.putInt(SETTING_NAME_LOWERFREQ, lowerFreq);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting lowerFreq to "
							+ lowerFreq);
				} else {
					// Lower freq not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no lowerFreq set.");
				}
				if (!c.isNull(5)) {
					int upperFreq = c.getInt(5);
					prefEditor.putInt(SETTING_NAME_UPPERFREQ, upperFreq);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting upperFreq to "
							+ upperFreq);
				} else {
					// Upper freq not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no upperFreq set.");
				}
				if (!c.isNull(6)) {
					int systolicOffset = c.getInt(6);
					prefEditor.putInt(
							PreferenceDatabase.SETTING_NAME_SYSOFFSET,
							systolicOffset);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(),
							"Setting systolicOffset to " + systolicOffset);
				} else {
					// systolicOffset not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no systolicOffset set.");
				}
				if (!c.isNull(7)) {
					int diastolicOffset = c.getInt(7);
					prefEditor.putInt(
							PreferenceDatabase.SETTING_NAME_DIASOFFSET,
							diastolicOffset);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(),
							"Setting diastolicOffset to " + diastolicOffset);
				} else {
					// diastolicOffset not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no diastolicOffset set.");
				}
				if (!c.isNull(8)) {
					String regressionMatrix = c.getString(8);
					prefEditor.putString(
							PreferenceDatabase.SETTING_NAME_REGMATRIX,
							regressionMatrix);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(),
							"Setting regression matrix to " + regressionMatrix);
				} else {
					// regression matrix not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no regression matrix set.");
				}
				if (!c.isNull(9)) {
					String pMatrix = c.getString(9);
					prefEditor.putString(
							PreferenceDatabase.SETTING_NAME_PMATRIX, pMatrix);
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "Setting P matrix to "
							+ pMatrix);
				} else {
					// p matrix not set
					Log.i(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(), "The user with id "
							+ user.id + " has no P matrix set.");
				}
			} else {
				// User not found
				Log.e(PreferenceDatabase.class.getSimpleName()
						+ "."
						+ Thread.currentThread().getStackTrace()[2]
								.getMethodName(), "The user with id " + user.id
						+ " was not found.");
				return;
			}

			// Commit the edit
			prefEditor.commit();
			c.close();

			// Save the fact that this is the current user
			currentlyLoadedUser = user;
		} else {
			Log.e(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"The database is not open.");
		}
	}

	public static void savePreferencesForUser(Context context, User user) {
		if (user == null) {
			Log.e(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"NULL user!");
			return;
		}
		if (isOpen) {
			// Get the shared preferences
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);

			// Store the values for the query
			ContentValues values = new ContentValues();
			if (prefs.contains(SETTING_NAME_AGE)) {
				values.put(UserTable.COLUMN_NAME_AGE, Float.parseFloat(prefs
						.getString(SETTING_NAME_AGE, "25")));
			}
			if (prefs.contains(SETTING_NAME_WEIGHT)) {
				values.put(UserTable.COLUMN_NAME_WEIGHT, Float.parseFloat(prefs
						.getString(SETTING_NAME_WEIGHT, "72")));
			}
			if (prefs.contains(SETTING_NAME_HEIGHT)) {
				values.put(UserTable.COLUMN_NAME_HEIGHT, Float.parseFloat(prefs
						.getString(SETTING_NAME_HEIGHT, "185")));
			}
			if (prefs.contains(SETTING_NAME_CONTINUOUS)) {
				values.put(UserTable.COLUMN_NAME_CONTINUOUS, prefs.getBoolean(
						SETTING_NAME_CONTINUOUS, false) ? 1 : 0);
			}
			if (prefs.contains(SETTING_NAME_LOWERFREQ)) {
				values.put(UserTable.COLUMN_NAME_LOWER_FREQ,
						prefs.getInt(SETTING_NAME_LOWERFREQ, Settings.DEF_LOWER_FREQ));
			}
			if (prefs.contains(SETTING_NAME_UPPERFREQ)) {
				values.put(UserTable.COLUMN_NAME_UPPER_FREQ,
						prefs.getInt(SETTING_NAME_UPPERFREQ, Settings.DEF_UPPER_FREQ));
			}
			if (prefs.contains(PreferenceDatabase.SETTING_NAME_SYSOFFSET)) {
				values.put(UserTable.COLUMN_NAME_SYSTOLIC_OFFSET, prefs.getInt(
						PreferenceDatabase.SETTING_NAME_SYSOFFSET, 0));
			}
			if (prefs.contains(PreferenceDatabase.SETTING_NAME_DIASOFFSET)) {
				values.put(UserTable.COLUMN_NAME_DIASTOLIC_OFFSET, prefs
						.getInt(PreferenceDatabase.SETTING_NAME_DIASOFFSET, 0));
			}
			if (prefs.contains(PreferenceDatabase.SETTING_NAME_REGMATRIX)) {
				values.put(UserTable.COLUMN_NAME_REG_MATRIX, prefs.getString(
						PreferenceDatabase.SETTING_NAME_REGMATRIX,
						RecursiveLeastSquareFilter
								.matrixToString(Settings.DEFAULT_REGRESSION)));
			}

			if (prefs.contains(PreferenceDatabase.SETTING_NAME_PMATRIX)) {
				values.put(UserTable.COLUMN_NAME_P_MATRIX, prefs.getString(
						PreferenceDatabase.SETTING_NAME_PMATRIX,
						RecursiveLeastSquareFilter
								.matrixToString(Settings.DEFAULT_P)));
			}
			// Only commit if more than zero values.
			if (values.size() > 0) {
				int rowsChanged = db.update(UserTable.TABLE_NAME, values,
						UserTable.COLUMN_NAME_ID + " = ?",
						new String[] { Integer.toString(user.id) });
				if (rowsChanged != 1) {
					Log.e(PreferenceDatabase.class.getSimpleName()
							+ "."
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName(),
							"Expected 1 row to be changed, but instead "
									+ rowsChanged + " were.");
				}
			}
		} else {
			Log.e(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"The database is not open.");
		}
	}

	public static void removeUser(User user) {
		if (user == null) {
			Log.e(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"NULL user!");
			return;
		}
		if (isOpen) {
			db.delete(UserTable.TABLE_NAME, UserTable.COLUMN_NAME_ID + " = ?",
					new String[] { Integer.toString(user.id) });
		} else {
			Log.e(PreferenceDatabase.class.getSimpleName() + "."
					+ Thread.currentThread().getStackTrace()[2].getMethodName(),
					"The database is not open.");
		}
	}

	public static User getCurrentlyLoadedUser() {
		return currentlyLoadedUser;
	}

	public static boolean isOpen() {
		return isOpen;
	}
}
