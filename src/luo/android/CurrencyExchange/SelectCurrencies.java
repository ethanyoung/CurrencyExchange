package luo.android.CurrencyExchange;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 选择货币
 * 带有复选框的ListView，使用boolean数组来实现复选框的初始化，
 * 返回的时候将复选框的状态存入Preferences中的"SelectedNation"
 * "SelectedNation"的数据类型是一个字符串，为了能够让其表示boolean数组，
 * 保存时，在每个boolean后面添加"#"号保存到"SelectedNation"中；
 * 读取时，使用字符串的split("#")方法，拆分字符串，保存到boolean数组中。
 * @author Administrator
 *
 */
public class SelectCurrencies extends ListActivity{
	
	ListView s_ListView;
	ArrayAdapter<String> s_Adapter;
	
	
	
	String[] item_select = new String[22];			//复选框ListView中的内容
	
	/**
	 * 程序入口
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("settings",0);
        
        //读取Preferences中的"SortSelect"参数，确定排序方式
        switch (settings.getInt("SortSelect", 0)) {
		
        //如果是按国家名排序
		case 0:
			
			for (int i=0;i<22;i++) {
				
				item_select[i] = ConstData.nation[i] +","+ ConstData.code[i];
			}
			
			//按照拼音首字母排序
			Arrays.sort(item_select, new Comparator<String>() {

				@Override
				public int compare(String object1, String object2) {
					// TODO Auto-generated method stub
					
					SharedPreferences settings = getSharedPreferences("settings",0);
					int result = 0;
					
					//读取"OrderSelect"，确定正序或者倒序
					switch (settings.getInt("OrderSelect", 0)) {
					case 0:
						result = Collator.getInstance(Locale.CHINA).compare(object1, object2);
						break;
					case 1:
						result = -Collator.getInstance(Locale.CHINA).compare(object1, object2);
						break;
					}
					return result;
				}
			});
			
			break;
			
		//如果是按货币代码排序
		case 1:
			
			for (int i=0;i<22;i++) {
				item_select[i] =  ConstData.code[i] +","+ ConstData.nation[i];
			}
			
			//按首字母排序
			Arrays.sort(item_select, new Comparator<String>() {

				@Override
				public int compare(String object1, String object2) {
					// TODO Auto-generated method stub
					
					SharedPreferences settings = getSharedPreferences("settings",0);
					int result = 0;
					
					//读取"OrderSelect"，确定正序或者倒序
					switch (settings.getInt("OrderSelect", 0)) {
					case 0:
						result = object1.compareTo(object2);
						break;
					case 1:
						result = -object1.compareTo(object2);
						break;
					}
					return result;
				}
			});
			
			break;
		}
		
        //创建列表界面
        s_Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, item_select);
        setListAdapter(s_Adapter);
        s_ListView = getListView();
        s_ListView.setItemsCanFocus(false);
        s_ListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        //初始化复选框
        initCheckbox(getSelectedNation());
    }
	
	/**
	 * 响应返回键
	 * 返回主界面之前，先将复选框的结果写入到字符串str_selected中，
	 * 再写入到Preferences的"SelectedNation"中
	 */
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			//获得ListView中的选择过的Item
			SparseBooleanArray Sparsechecked = s_ListView.getCheckedItemPositions();
			
			//用于存放对应22个国家的boolean值，true表示选择了，false表示没有选择。
			boolean selected[] = new boolean[ConstData.code.length];
			
			//将选择为true的Item所对应的国家，写入true到selected中，其他写入false
			for(int i=0;i<ConstData.code.length;i++) {
				for(int j=0;j<Sparsechecked.size();j++) {
					if (Sparsechecked.valueAt(j) && 
							s_Adapter.getItem(Sparsechecked.keyAt(j)).contains(ConstData.code[i])) {
						selected[i] = true;
						break;
					}
					selected[i] = false;
				}
			}
			
			//将boolean数组selected，写入到字符串str_selected
			String str_selected = new String();
			for(int i=0;i<selected.length;i++) {
				str_selected += String.valueOf(selected[i]) + "#";
			}
			
			//存入Preferences的"SelectedNation"中
			SharedPreferences settings = getSharedPreferences("settings",0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("SelectedNation", str_selected);
		    editor.commit();
			
		    //切换界面
			Intent intent = new Intent();
			intent.setClass(SelectCurrencies.this, CurrencyExchange.class);
			startActivity(intent);
			SelectCurrencies.this.finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 读取Preferences的"SelectedNation"，确定对应22国家的boolean数组。
	 * @return nation[]:选中国家的货币代码返回nation。
	 */
	public String[] getSelectedNation () {
		
		boolean selected_nation[] = new boolean [22];	//表示国家是否被选中

		String nation [];	//存放选中国家的货币代码
		int count = 0;		//用于表示有多少国家需要标记为true
		
		//读取Preferences的"SelectedNation"，调用split()方法，存入boolean数组
		SharedPreferences settings = getSharedPreferences("settings",0);
		String def = "true#true#true#true#true#true#true#true" +
					"#false#false#false#false#false#false#false#false" +
					"#false#false#false#false#false#false#";
		String [] selected = settings.getString("SelectedNation", def).split("#");
		for (int i=0;i<selected_nation.length;i++) {
			selected_nation[i] = Boolean.parseBoolean(selected[i]);
			if (selected_nation[i]) {
				count++;
			}
		}
		
		//获得需要显示为true的国家的货币代码
		nation = new String[count];
		int j=0;
		for (int i=0;i<ConstData.code.length;i++) {
			if (selected_nation[i]) {
				
				nation [j] = ConstData.code[i];
				++j;
			}
		}
		
		return nation;
	}
	
	/**
	 * 根据nation中的值，初始化ListView中的复选框
	 * @param nation[]:需要显示为true的国家的货币代码
	 */
	public void initCheckbox (String nation[]) {
		
		for (int i=0;i<nation.length;i++) {
			
			for (int j=0;j<s_Adapter.getCount();j++) {
				
				//复选框中的默认值为false，给nation对应的Item设置为true即可
				if (s_Adapter.getItem(j).contains(nation[i])) {
					s_ListView.setItemChecked(j, true);
				}
			}
		}
	}
}