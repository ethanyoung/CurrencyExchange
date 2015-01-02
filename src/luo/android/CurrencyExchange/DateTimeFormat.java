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
 * 设置时间日期格式
 * 形式为列表。有两列：日期格式，时间格式。
 * 单击选项后，弹出响应的单选对话框提供选择。
 * 运行时涉及到Shared Preferences中的"DateFormat","TimeFormat"的读取和写入。
 * @author Administrator
 *
 */

public class DateTimeFormat extends Activity {
	
	//用于显示列表
	LinearLayout n_LinearLayout;
	ListView n_ListView;
	ArrayAdapter<String> n_Adapter;
	
	//用于显示对话框
	AlertDialog FormatDlg;
	
	//对话框中的选项
	final String[] item_Date = {"MM-DD-YYYY","DD-MM-YYYY","YYYY-MM-DD"};
	final String[] item_Time = {"12小时制","24小时制",};
	
	/**
	 * 用于响应返回键
	 */
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(DateTimeFormat.this, Settings.class);
			startActivity(intent);
			DateTimeFormat.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 用于给列表添加内容
	 * @return：包含内容的LIst
	 */
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("日期格式");
		data.add("时间格式");
		
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
		
		//给LIstView中的Item添加单击事件侦听
		n_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3)  {
        		SharedPreferences settings = getSharedPreferences("settings",0);
        		
        		switch (arg2) {
        		
        		//如果选择了“日期格式”
        		case 0:
        			AlertDialog.Builder builder_Decimals = new AlertDialog.Builder(DateTimeFormat.this);
        			builder_Decimals.setTitle("日期格式");
        			builder_Decimals.setSingleChoiceItems(item_Date, settings.getInt("DateFormat", 2), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									//将单击的选项序号存入"DateFormat"
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("DateFormat", which);
								    editor.commit();
								}
								
        			});
        			FormatDlg = builder_Decimals.create();
        			FormatDlg.show();
        			break;
        			
        		//如果选择了“时间格式”
        		case 1:
        			AlertDialog.Builder builder_Grouping = new AlertDialog.Builder(DateTimeFormat.this);
        			builder_Grouping.setTitle("时间格式");
        			builder_Grouping.setSingleChoiceItems(item_Time, settings.getInt("TimeFormat", 1), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									//将单击的选项号存入"TimeFormat"
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("TimeFormat", which);
								    editor.commit();
								}
        			});
        			FormatDlg = builder_Grouping.create();
        			FormatDlg.show();
        			break;
        		}
        	}
        });
	}
}

