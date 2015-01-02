package luo.android.CurrencyExchange;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 杂项
 * 形式为列表。有两列：税额，启动项
 * 单击“税额”，转换到TaxAmount.java运行
 * 单击“启动项”，弹出对话框选择是否需要下次启动保留上一次输入的数据
 * 涉及到Shared Preferences中的"UseLastAmount"的读取和写入
 * @author Administrator
 *
 */

public class Miscellaneous extends Activity {

	//用于显示列表
	LinearLayout n_LinearLayout;
	ListView n_ListView;
	ArrayAdapter<String> n_Adapter;
	
	//用于显示对话框
	AlertDialog MislsDlg;
	
	//对话框中的选项
	final String[] item_startup = {"启动程序时使用上一次的数额"};
	
	/**
	 * 响应返回键
	 */
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(Miscellaneous.this, Settings.class);
			startActivity(intent);
			Miscellaneous.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 给列表添加内容
	 * @return：包含内容的List
	 */
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("税额");
		data.add("启动项");
		
		return data;
	}
	
	/**
	 * 程序入口
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//创建列表界面
		n_LinearLayout = new LinearLayout(this);
		n_ListView = new ListView(this);
		n_Adapter = new ArrayAdapter<String>(this,android.R.
				layout.simple_expandable_list_item_1,getData());
		n_ListView.setAdapter(n_Adapter);
		setContentView(n_ListView);
		
		//ListView中的Item的单击事件侦听器
		n_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3)  {
        		
        		switch (arg2) {
        		
        		//如果选项是“税额”
        		case 0:
        			
        			//转到TaxAmount.java
        			Intent intent = new Intent();
        			intent.setClass(Miscellaneous.this, TaxAmount.class);
        			startActivity(intent);
        			Miscellaneous.this.finish();
        			
        			break;
        		
        		//如果选项是“启动项”
        		case 1:

        			//使用一个复选对话框，来表示是否需要启动项
            		SharedPreferences settings = getSharedPreferences("settings",0);
            		boolean[] ItemChecked = {settings.getBoolean("UseLastAmount", false)};
            		MislsDlg = new AlertDialog.Builder(Miscellaneous.this)
        			.setTitle("设置启动项")
        			.setMultiChoiceItems(item_startup, ItemChecked, 
        					new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									
									// TODO Auto-generated method stub
									//将勾选的结果存入"UserLastAmount"
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putBoolean("UseLastAmount", isChecked);
								    editor.commit();
								}
        			})
        			.create();
        			MislsDlg.show();
        			break;
        		}
        	}
        });
	}
}

