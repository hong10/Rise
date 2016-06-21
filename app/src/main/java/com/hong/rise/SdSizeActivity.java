package com.hong.rise;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Administrator on 2016/6/20.
 */
public class SdSizeActivity extends Activity {
    private static final int TOTAL_SIZE = 0;
    private static final int AVAIL_SIZE = 1;
    private TextView sdTotalSize, sdAvailSize, romTotalSize, romAvailSize, dataTotalSize, dataAvailSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sd_size);
        sdTotalSize = (TextView) findViewById(R.id.tv_sd_total_size);
        sdAvailSize = (TextView) findViewById(R.id.tv_sd_avail_size);
        romTotalSize = (TextView) findViewById(R.id.tv_rom_total_size);
        romAvailSize = (TextView) findViewById(R.id.tv_rom_avail_size);
        dataTotalSize = (TextView) findViewById(R.id.tv_data_total_size);
        dataAvailSize = (TextView) findViewById(R.id.tv_data_avail_size);
    }


    public void getSize(View view) {

        StatFs statFs = null;
        File path = null;
        long blockSize = 0;
        String size = null;
        switch (view.getId()) {

            //get sd total size
            case R.id.bt_sd_total_size:
                sdTotalSize.setText("sd total: " + getSizeByPath(SdSizeActivity.this, Environment.getExternalStorageDirectory(), TOTAL_SIZE));
                break;

            //get sd avail size
            case R.id.bt_sd_avail_size:
                sdAvailSize.setText("sd avail: " + getSizeByPath(SdSizeActivity.this, Environment.getExternalStorageDirectory(), AVAIL_SIZE));

                break;

            //get rom total size(/system)
            case R.id.bt_rom_total_size:
                romTotalSize.setText("rom total: " + getSizeByPath(SdSizeActivity.this, Environment.getRootDirectory(), TOTAL_SIZE));
                break;

            //get rom avail size(/system)
            case R.id.bt_rom_avail_size:
                romAvailSize.setText("rom avail: " + getSizeByPath(SdSizeActivity.this, Environment.getRootDirectory(), AVAIL_SIZE));
                break;

            //get data total size(/data)
            case R.id.bt_data_total_size:
                dataTotalSize.setText("data total: " + getSizeByPath(SdSizeActivity.this, Environment.getDataDirectory(), TOTAL_SIZE));
                break;

            //get data avail size(/data)
            case R.id.bt_data_avail_size:
                dataAvailSize.setText("data avail: " + getSizeByPath(SdSizeActivity.this, Environment.getDataDirectory(), AVAIL_SIZE));
                break;
        }
    }

    private String getSizeByPath(Context context, File file, int type) {

        if (file != null) {
            String size = null;
            StatFs statFs = new StatFs(file.getPath());
            long blockSize = statFs.getBlockSize();

            if (type == TOTAL_SIZE) {
                long totalBlocks = statFs.getBlockCount();
                size = Formatter.formatFileSize(context, blockSize * totalBlocks);
                return size;
            }
            if (type == AVAIL_SIZE) {
                long availBlocks = statFs.getAvailableBlocks();
                size = Formatter.formatFileSize(context, blockSize * availBlocks);
                return size;
            }
            return "type error";

        } else {
            return null;
        }
    }

}
