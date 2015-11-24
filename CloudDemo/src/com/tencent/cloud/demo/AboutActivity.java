package com.tencent.cloud.demo;

import java.util.ArrayList;
import java.util.List;

import com.tencent.cloud.demo.common.BizService;
import com.tencent.upload.common.Global;
import com.tencent.upload.log.trace.TracerConfig;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 关于页面
 *
 */
public class AboutActivity extends Activity {
	
	private ListView mListView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }
	
	private void initUI()
    {
        setContentView(R.layout.activity_about);
        mListView = (ListView) findViewById(R.id.listview);
    }
	
	private void initData()
	{
		List<Item> items = new ArrayList<Item>();
		items.add(new Item("APPID", BizService.APPID));
		items.add(new Item("IMEI", Global.getDeviceId()));
		items.add(new Item("APP版本", BizService.APP_VERSION));
		items.add(new Item("上传SDK版本", Global.SDK_VERSION));
		items.add(new Item("下载SDK版本", com.tencent.download.global.Global.SDK_VERSION));
		items.add(new Item("日志文件路径", TracerConfig.getLogDir(getApplicationContext())));
		
		MyBaseAdapter adapter = new MyBaseAdapter(this, items);
		mListView.setAdapter(adapter);
	}
	
	class Item {
		public String title;
		public String content;
		
		public Item(String title, String content)
		{
			this.title = title;
			this.content = content;
		}
	}

	
	public class MyBaseAdapter extends BaseAdapter {

		private List<Item> values;
		private Context context;

		public MyBaseAdapter(Context context, List<Item> persons) {
			this.values = persons;
			this.context = context;
		}

		@Override
		public int getCount() {
			return (values == null) ? 0 : values.size();
		}

		@Override
		public Object getItem(int position) {
			return values.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public class ViewHolder {
			TextView title;
			TextView content;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Item value = (Item) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.view_aboutlist_item, null);

				viewHolder = new ViewHolder();
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.content);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.title.setText(String.valueOf(value.title));
			viewHolder.content.setText(value.content);

			return convertView;
		}
	}
	
}
