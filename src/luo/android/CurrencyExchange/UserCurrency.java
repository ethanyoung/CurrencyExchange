package luo.android.CurrencyExchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用户货币设置界面
 * 可以设置：货币名称，货币简写，货币符号，与一个常用货币对比的汇率以及选择用哪个常用货币作对比。
 * 涉及到Preferences中的"UserName"，"UserCode"，"UserSymbol"，"UserRate"，"UserCompare"的读写。
 * @author Administrator
 *
 */
public class UserCurrency extends Activity{
	
	EditText user_name;
	EditText user_code;
	EditText user_symbol;
	EditText user_rate;
	
	TextView compare_currency;
	
	Button Btn_user_change;
	
	AlertDialog SltNatnDlg;
	
	/**
	 * 响应返回键
	 */
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			//返回前，将各个数值写入到Preferences对应的参数中
			SharedPreferences settings = getSharedPreferences("settings",0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("UserName", user_name.getText().toString());
		    editor.putString("UserCode", user_code.getText().toString());
		    editor.putString("UserSymbol", user_symbol.getText().toString());
		    editor.putFloat("UserRate", Float.parseFloat(user_rate.getText().toString()));
		    editor.commit();
		    
		    //返回“货币设置”
			Intent intent = new Intent();
			intent.setClass(UserCurrency.this, CurrencySettings.class);
			startActivity(intent);
			UserCurrency.this.finish();
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_currency);
		
		user_name = (EditText) findViewById(R.id.user_name);
		user_code = (EditText) findViewById(R.id.user_code);
		user_symbol = (EditText) findViewById(R.id.user_symbol);
		user_rate = (EditText) findViewById(R.id.user_rate);
		
		compare_currency = (TextView) findViewById(R.id.compare_currency);

		Btn_user_change = (Button) findViewById(R.id.user_change);
		
		SharedPreferences settings = getSharedPreferences("settings",0);
		
		//设置各个编辑框的初始值
		user_name.setText(settings.getString("UserName", "用户货币"));
		user_code.setText(settings.getString("UserCode", "USY"));
		user_symbol.setText(settings.getString("UserSymbol", "¥"));
		user_rate.setText(String.valueOf( settings.getFloat("UserRate", 1.0f)));
		
		String compare = "1 " + ConstData.code[settings.getInt("UserCompare", 1)] + " =  X";
		
		compare_currency.setText(compare);
		
		//更换与用户货币比较的货币
		Btn_user_change.setOnClickListener(new Button.OnClickListener() {
			@Override
        	public void onClick(View v) {
				
				String item [] = new String[22];
				for(int i=0;i<item.length;i++) {
					item[i] = ConstData.code[i] +","+ ConstData.nation[i];
				}
				
				SharedPreferences settings = getSharedPreferences("settings",0);
        		SltNatnDlg = new AlertDialog.Builder(UserCurrency.this)
        		.setTitle("选择比较的货币")
				.setSingleChoiceItems(item, settings.getInt("UserCompare", 1), 
						new DialogInterface.OnClickListener () {
					
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								// TODO Auto-generated method stub
								//选择的值存入"UserCompare"
								SharedPreferences settings = getSharedPreferences("settings",0);
							    SharedPreferences.Editor editor = settings.edit();
							    editor.putInt("UserCompare", which);
							    editor.commit();
							    
							    //修改显示的比较货币
							    String compare = "1 " + ConstData.code[settings.getInt("UserCompare", 1)] + " =  X";
								compare_currency.setText(compare);
								
							}
					
				})
				.create();
				SltNatnDlg.show();
        	}
        	
        });
        
		
	}

}
