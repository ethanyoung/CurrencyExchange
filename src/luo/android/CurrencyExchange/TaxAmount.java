package luo.android.CurrencyExchange;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 税额
 * 编辑税额（百分计），允许正负。
 * 涉及到Preferences的"Tax"的读写。
 * @author Administrator
 *
 */
public class TaxAmount extends Activity {

	EditText edit;		//用于写入税额值得编辑框
	Button clear;		//清零按钮
	
	/**
	 * 响应返回键、“+”、“-”
	 * 返回“设置界面”之前，先把税额存入Preferences的"Tax"中
	 */
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		edit = (EditText) findViewById(R.id.tax_amount);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			
			SharedPreferences settings = getSharedPreferences("settings",0);
			SharedPreferences.Editor editor = settings.edit();
			//编辑框为空，则写入0
			if (edit.getText().length() == 0) {
				editor.putFloat("Tax", 0);
			}
			else {
			    editor.putFloat("Tax", Float.parseFloat(edit.getText().toString()));
			}
			editor.commit();
			
			Intent intent = new Intent();
			intent.setClass(TaxAmount.this, Miscellaneous.class);
			startActivity(intent);
			TaxAmount.this.finish();
			
			break;
			
		//“+”键，则去掉符号
		case KeyEvent.KEYCODE_EQUALS:
			if(edit.getText().toString().contains("-"))
				edit.setText(edit.getText().toString().substring(1));
			break;
		
		//"-"键，给值添加符号
		case KeyEvent.KEYCODE_MINUS:
			if(!edit.getText().toString().contains("-"))
				edit.setText("-" + edit.getText());
				
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tax_amount);
		edit = (EditText) findViewById(R.id.tax_amount);
		clear = (Button) findViewById(R.id.tax_clear);
		
		//初始化编辑框
		SharedPreferences settings = getSharedPreferences("settings",0);
		edit.setText(String.valueOf(settings.getFloat("Tax", 0)));
		
		//编辑框，只接受数字和小数点输入
		edit.setKeyListener(new DigitsKeyListener(false,true));
		
		//清零按钮的单击
		clear.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				edit.setText("");
			}
			
		});
		
	}
}
