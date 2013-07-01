package com.blstream.urbangame.fragments;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blstream.urbangame.R;

public class LoginRegisterView extends LinearLayout implements OnClickListener {
	public final static int TYPE_LOGIN = 0;
	public final static int TYPE_REGISTER = 1;
	
	private final int PASSWORD_HIDE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
	private final int PASSWORD_SHOW = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
	
	private final String BUTTON_LOGIN;
	private final String BUTTON_REGISTER;
	
	private TextView editTextEmail;
	private EditText editTextPassword;
	private EditText editTextDisplayName;
	private Button buttonLoginRegister;
	private CheckBox checkBoxShowPassword;
	private CompositeOnClickListener compositeOnClickListener;
	
	private int type;
	
	public LoginRegisterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		BUTTON_LOGIN = context.getString(R.string.button_login);
		BUTTON_REGISTER = context.getString(R.string.button_register);
		
		setContentView(context);
		initializeComponents();
		
		type = getType(context, attrs);
		configureViewAndComponents();
	}
	
	private void setContentView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fragment_login_register, this, true);
	}
	
	private void initializeComponents() {
		compositeOnClickListener = new CompositeOnClickListener();
		compositeOnClickListener.addListener(this);
		
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextDisplayName = (EditText) findViewById(R.id.editTextDisplayName);
		
		buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);
		buttonLoginRegister.setOnClickListener(compositeOnClickListener);
		
		checkBoxShowPassword = ((CheckBox) findViewById(R.id.checkBoxShowPassword));
		checkBoxShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				showPassword(isChecked);
			}
		});
	}
	
	private int getType(Context context, AttributeSet attrs) {
		Theme theme = context.getTheme();
		TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.LoginRegisterView, 0, 0);
		
		int type;
		try {
			type = a.getInteger(R.styleable.LoginRegisterView_type, 0);
		}
		finally {
			a.recycle();
		}
		return type;
	}
	
	private void configureViewAndComponents() {
		switch (type) {
			case TYPE_LOGIN:
				configureForLogin();
				break;
			case TYPE_REGISTER:
				configureForRegister();
				break;
		}
	}
	
	private void configureForLogin() {
		showDisplayName(false);
		settButtonText(BUTTON_LOGIN);
	}
	
	private void configureForRegister() {
		showDisplayName(true);
		settButtonText(BUTTON_REGISTER);
	}
	
	private void showDisplayName(boolean show) {
		editTextDisplayName.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	private void settButtonText(String buttonText) {
		buttonLoginRegister.setText(buttonText);
	}
	
	private void showPassword(boolean show) {
		editTextPassword.setInputType(show ? PASSWORD_SHOW : PASSWORD_HIDE);
		setCursorAtTheEndOfText();
	}
	
	private void setCursorAtTheEndOfText() {
		editTextPassword.setSelection(editTextPassword.getText().length());
	}
	
	public void setType(int type) {
		this.type = type;
		configureViewAndComponents();
		invalidate();
		requestLayout();
	}
	
	public void setOnButtonClickListener(OnClickListener listener) {
		compositeOnClickListener.addListener(listener);
	}
	
	public String getEmail() {
		return editTextEmail.getText().toString();
	}
	
	public String getPassword() {
		return editTextPassword.getText().toString();
	}
	
	public String getDisplayName() {
		return editTextDisplayName.getText().toString();
	}
	
	private class CompositeOnClickListener implements OnClickListener {
		private final Set<OnClickListener> delegates = new HashSet<OnClickListener>();
		
		public void addListener(OnClickListener listener) {
			delegates.add(listener);
		}
		
		@Override
		public void onClick(View v) {
			for (OnClickListener listener : delegates) {
				listener.onClick(v);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		String email = getEmail();
		if (!isEmailAddress(email)) {
			editTextEmail.setTextColor(Color.RED);
		}
		else {
			editTextEmail.setTextColor(Color.BLACK);
		}
	}
	
	public boolean isDataSyntaxCorrect() {
		switch (type) {
			case TYPE_LOGIN:
				return isLoginDataValid(getEmail(), getPassword());
			case TYPE_REGISTER:
				return isRegisterDataValid(getEmail(), getDisplayName(), getPassword());
		}
		return false;
	}
	
	private boolean isLoginDataValid(String email, String password) {
		if (isEmpty(email) || isEmpty(password)) return false;
		if (!isEmailAddress(email)) return false;
		return true;
	}
	
	private boolean isRegisterDataValid(String email, String displayName, String password) {
		return isLoginDataValid(email, password) && !isEmpty(displayName);
	}
	
	private boolean isEmpty(String string) {
		return string.length() == 0;
	}
	
	private boolean isEmailAddress(String email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}