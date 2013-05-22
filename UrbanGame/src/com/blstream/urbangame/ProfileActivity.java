package com.blstream.urbangame;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.entity.Player;

public class ProfileActivity extends MenuActivity {
	
	private static final String TAG = ProfileActivity.class.getSimpleName();
	
	private static final int IMAGE_REQUEST = 1;
	private static final String AVATAR_BUNDLE_KEY = "avatar";
	private static final String DISPLAY_NAME_BUNDLE_KEY = "displayName";
	private static final float MAX_AVATAR_SIZE = 400;
	
	private ImageView imageViewProfileImage;
	private EditText editTextDisplayName;
	
	private Bitmap avatarBitmap;
	private String savedDisplayName;
	
	private Database dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//database helper
		dbHelper = new Database(this);
		
		//avatar image view
		imageViewProfileImage = (ImageView) findViewById(R.id.imageViewProfileImage);
		imageViewProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showChooseAvatarDialog();
			}
			
		});
		
		//profile display name
		editTextDisplayName = (EditText) findViewById(R.id.editTextDislayName);
		
		//check saved bitmap after phone orientation change
		if (savedInstanceState != null) {
			avatarBitmap = savedInstanceState.getParcelable(AVATAR_BUNDLE_KEY);
			savedDisplayName = savedInstanceState.getString(DISPLAY_NAME_BUNDLE_KEY);
		}
		
		//save button
		Button saveButton = (Button) findViewById(R.id.buttonProfileSaveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Save button clicked");
				saveProfileInfo();
				startMyGamesActivity();
			}
		});
		
		//skip button
		Button skipButton = (Button) findViewById(R.id.buttonProfileSkipButton);
		skipButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Skip button clicked");
				startMyGamesActivity();
			}
		});
	}
	
	private void startMyGamesActivity() {
		avatarBitmap = null;
		savedDisplayName = null;
		
		Intent intent = new Intent(ProfileActivity.this, MyGamesActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Player loggedPlayer = null;
		String loggedPlayerID = dbHelper.getLoggedPlayerID();
		
		if (loggedPlayerID != null) {
			loggedPlayer = dbHelper.getPlayer(loggedPlayerID);
		}
		
		//restore avatar
		if (avatarBitmap != null) {
			imageViewProfileImage.setImageBitmap(avatarBitmap);
		}
		else if (loggedPlayer != null && loggedPlayer.getAvatarBase64() != null) {
			Drawable avatarDrawable = loggedPlayer.getAvatar(getResources());
			imageViewProfileImage.setImageDrawable(avatarDrawable);
		}
		else {
			imageViewProfileImage.setImageResource(R.drawable.choose_avatar);
		}
		
		//restore display name
		if (savedDisplayName != null) {
			editTextDisplayName.setText(savedDisplayName);
		}
		else if (loggedPlayer != null && loggedPlayer.getDisplayName() != null) {
			editTextDisplayName.setText(loggedPlayer.getDisplayName());
		}
		else {
			editTextDisplayName.setText(null);
		}
		
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	/**
	 * Create dialog for user to choose source of avatar (gallery or camera).
	 */
	private void showChooseAvatarDialog() {
		Log.d(TAG, "Choose avatar dialog showed");
		AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
		builder.setTitle(R.string.profile_activity_dialog_title).setItems(R.array.profile_activty_dialog_options,
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case 0:
							Log.d(TAG, "clicked choose avatar from gallery");
							Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
							photoPickerIntent.setType("image/*");
							startActivityForResult(photoPickerIntent, IMAGE_REQUEST);
							break;
						case 1:
							Log.d(TAG, "clicked get avatar from camera");
							Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(takePictureIntent, IMAGE_REQUEST);
							break;
					}
				}
			});
		builder.create().show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST) {
			Uri selectedImageUri = intent.getData();
			InputStream imageStream;
			try {
				imageStream = getContentResolver().openInputStream(selectedImageUri);
				avatarBitmap = scaleBitmap(BitmapFactory.decodeStream(imageStream));
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (avatarBitmap != null) {
				imageViewProfileImage.setImageBitmap(avatarBitmap);
			}
		}
		
	}
	
	private void saveProfileInfo() {
		//store in db
		String loggedPlayerEmail = dbHelper.getLoggedPlayerID();
		String displayName = editTextDisplayName.getText().toString();
		Log.d(TAG, "Saving new display name: " + displayName);
		Drawable avatarDrawable = null;
		if (avatarBitmap != null) {
			avatarDrawable = new BitmapDrawable(getResources(), avatarBitmap);
			Log.d(TAG, "Saving new avatar");
		}
		Player player = new Player(loggedPlayerEmail, null, displayName, avatarDrawable);
		dbHelper.updatePlayer(player);
		Log.d(TAG, "New profile info stored in database");
		
		//TODO store in webserver, should be done when web api will be released
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (avatarBitmap != null) {
			outState.putParcelable(AVATAR_BUNDLE_KEY, avatarBitmap);
		}
		if (editTextDisplayName.getText() != null) {
			outState.putString(DISPLAY_NAME_BUNDLE_KEY, editTextDisplayName.getText().toString());
		}
	}
	
	/**
	 * Scale bitmap to fixed size if it's too large
	 * 
	 * @param bitmap
	 * @return
	 */
	private Bitmap scaleBitmap(Bitmap bitmap) {
		Bitmap scaledBitmap = bitmap;
		Log.d(TAG, "scale bitmap before: " + scaledBitmap.getWidth() + "x" + scaledBitmap.getHeight());
		if (bitmap.getWidth() > MAX_AVATAR_SIZE || bitmap.getHeight() > MAX_AVATAR_SIZE) {
			float scaleFactor = bitmap.getWidth() > bitmap.getHeight() ? (MAX_AVATAR_SIZE / bitmap.getWidth())
				: (MAX_AVATAR_SIZE / bitmap.getHeight());
			Log.d(TAG, "scale bitmap factor: " + scaleFactor);
			int newWidth = (int) (bitmap.getWidth() * scaleFactor);
			int newHeight = (int) (bitmap.getHeight() * scaleFactor);
			scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
		}
		Log.d(TAG, "scale bitmap after: " + scaledBitmap.getWidth() + "x" + scaledBitmap.getHeight());
		return scaledBitmap;
	}
}
