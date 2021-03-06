package cn.cerc.summer.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mimrc.vine.R;

import cn.cerc.summer.android.Entity.Config;
import cn.cerc.summer.android.MyConfig;
import cn.cerc.summer.android.Utils.Constans;
import cn.cerc.summer.android.Utils.ScreenUtils;
import cn.cerc.summer.android.View.CustomSeekBar;

public class SettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private TextView url_tit, scale;
    private EditText edittext;
    private CustomSeekBar customseekbar;
    private Button button, recover;
    private ImageView back;
    private int scales = 0;
    private int def_scales = 0;
    private LinearLayout lin_cun;

    private Button[] buttons = new Button[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        def_scales = ScreenUtils.getScales(this, ScreenUtils.getInches(this));
        back = (ImageView) this.findViewById(R.id.back);
        button = (Button) this.findViewById(R.id.save);
        edittext = (EditText) this.findViewById(R.id.url);
        url_tit = (TextView) this.findViewById(R.id.url_tit);
        scale = (TextView) this.findViewById(R.id.scale);
        recover = (Button) this.findViewById(R.id.recover);
        customseekbar = (CustomSeekBar) this.findViewById(R.id.customseekbar);
        customseekbar.setOnSeekBarChangeListener(this);
        if (null == getIntent().getStringExtra("address"))
            edittext.setText(settingShared.getString(Constans.HOME, ""));
        else edittext.setText(getIntent().getStringExtra("address"));
        scales = settingShared.getInt(Constans.SCALE_SHAREDKEY, def_scales);
        customseekbar.setProgress(scales);

        lin_cun = (LinearLayout) this.findViewById(R.id.lin_cun);

        if (!Config.getConfig().isDebug()) {
            url_tit.setVisibility(View.GONE);
            edittext.setVisibility(View.GONE);
//            lin_cun.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("scale", scales);
                intent.putExtra("home", edittext.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edittext.getText().toString().trim()) && !edittext.getText().toString().trim().contains("http"))
                    Toast.makeText(SettingActivity.this, R.string.no_http_tips, Toast.LENGTH_SHORT).show();
                else
                    settingShared.edit().putString(Constans.HOME, edittext.getText().toString().trim()).commit();
                if (scales == 0)
                    settingShared.edit().putInt(Constans.SCALE_SHAREDKEY, scales).commit();
                else settingShared.edit().putInt(Constans.SCALE_SHAREDKEY, scales).commit();
                MainActivity.getInstance().reload(scales);
                Toast.makeText(v.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingShared.edit().putString(Constans.HOME, MyConfig.HOME_URL).putInt(Constans.SCALE_SHAREDKEY, def_scales).commit();
                customseekbar.setProgress(def_scales);
                MainActivity.getInstance().reload(def_scales);
                Toast.makeText(v.getContext(), "已恢复默认", Toast.LENGTH_SHORT).show();
            }
        });

        buttons[0] = (Button) this.findViewById(R.id.button1);
        buttons[1] = (Button) this.findViewById(R.id.button2);
        buttons[2] = (Button) this.findViewById(R.id.button3);
        buttons[3] = (Button) this.findViewById(R.id.button4);
        buttons[4] = (Button) this.findViewById(R.id.button5);

        for (Button button : buttons)
            button.setOnClickListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        scales = (progress + 80);//其中70是设置的最小值
        String str = String.format("界面缩放比例（80%% -- 100%%）当前值：%d%%", scales);
        scale.setText(str);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                scales = ScreenUtils.getScales(this, 4);
                break;
            case R.id.button2:
                scales = ScreenUtils.getScales(this, 4.5);
                break;
            case R.id.button3:
                scales = ScreenUtils.getScales(this, 5);
                break;
            case R.id.button4:
                scales = ScreenUtils.getScales(this, 5.5);
                break;
            case R.id.button5:
                scales = ScreenUtils.getScales(this, 6);
                break;
        }
        customseekbar.setProgress(scales);
        String str = String.format("界面缩放比例（80%% -- 100%%）当前值：%d%%", scales);
        scale.setText(str);
    }
}
