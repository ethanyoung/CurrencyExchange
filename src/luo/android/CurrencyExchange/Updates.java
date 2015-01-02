package luo.android.CurrencyExchange;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 更新界面
 * 设置程序两次自动更新间隔的时间
 * 涉及到"Updates"文件的写入和读取
 * @author Administrator
 *
 */
public class Updates extends Activity{

		EditText edit;		//更新间隔时间的编辑框
		Button cancel;		//取消自动更新按钮
		
		/**
		 * 响应返回键
		 * 返回“设置界面”之前，先把税额存入Preferences的"Updates"中
		 */
		public boolean onKeyDown (int keyCode, KeyEvent event) {
			edit = (EditText) findViewById(R.id.updates_amount);
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				
				SharedPreferences settings = getSharedPreferences("settings",0);
				SharedPreferences.Editor editor = settings.edit();
				int updates = Integer.parseInt(edit.getText().toString());
				
				//编辑框为空，则写入0
				if (edit.getText().length() == 0) {
					editor.putInt("Updates", 0);
				}
				//24到48之间的数字直接写入
				else if (24<=updates && updates<=48){
					editor.putInt("Updates", updates);
				}
				//小于24以24计
				else if (updates<24){
					editor.putInt("Updates", 24);
				}
				//大于48以48计
				else if (updates>48) {
					editor.putInt("Updates", 48);
				}
				editor.commit();
				
				Intent intent = new Intent();
				intent.setClass(Updates.this, CurrencySettings.class);
				startActivity(intent);
				Updates.this.finish();
				
				break;
			}
			return super.onKeyDown(keyCode, event);
		}
		
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.updates);
			edit = (EditText) findViewById(R.id.updates_amount);
			cancel = (Button) findViewById(R.id.cancel_auto);
			
			//初始化编辑框
			SharedPreferences settings = getSharedPreferences("settings",0);
			edit.setText(String.valueOf(settings.getInt("Updates", 0)));
			
			//取消更新按钮的单击
			cancel.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//"Updates"写入0
					SharedPreferences settings = getSharedPreferences("settings",0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("Updates", 0);
					editor.commit();
				
					//转换界面
					Intent intent = new Intent();
					intent.setClass(Updates.this, CurrencySettings.class);
					startActivity(intent);
					Updates.this.finish();
				}
				
			});
			
		}
	}
