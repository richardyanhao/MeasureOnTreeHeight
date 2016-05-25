package com.measureonhigh;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Editable;
import android.view.*;
import android.widget.*;

import java.math.BigDecimal;

import com.example.db.*;

public class MainActivity extends Activity 
implements SensorEventListener,SurfaceHolder.Callback,View.OnClickListener
{
	private MySQLiteOpenHelper mySQLiteOpenHelper;
	private SurfaceView mySurfaceView;
	private Sensor sensor;
	private SensorManager sensorMgr;
	private Toast mToast;
	private Camera myCamera;
	private SurfaceHolder mySurfaceHolder;
	private Button mLockButtomBtn;
	private Button topButton;
	private Button mSettingBtn;
	private float y = 0.0F;
	private float L = 1.7F;
	private EditText mInputHeightET;
	private TextView mInputTv;
	int state=0;
	int t1=0,t2=0,t3=0;
	int s1=0,s2=0,s3=0;
	
	String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mySurfaceView = ((SurfaceView)findViewById(R.id.mySurfaceView));
        this.mySurfaceHolder = this.mySurfaceView.getHolder();
        this.mySurfaceHolder.addCallback(this);
        this.mySurfaceHolder.setType(3);
        this.mLockButtomBtn = ((Button)findViewById(R.id.lockbuttombtn));
        this.mLockButtomBtn.setOnClickListener(this);
        this.topButton=((Button)findViewById(R.id.top));
        this.topButton.setOnClickListener(this);
        this.mSettingBtn = ((Button)findViewById(R.id.settingbtn));
        this.mSettingBtn.setOnClickListener(this);
        this.sensorMgr = ((SensorManager)getSystemService("sensor"));
        this.sensor = this.sensorMgr.getDefaultSensor(3);
        this.sensorMgr.registerListener(this, this.sensor, 0);
        this.mToast = Toast.makeText(this, "", 0);
        this.mToast.show();
        init();
        Intent intent = getIntent();
        result=intent.getStringExtra("result");
    }
    private void init() {
		// TODO Auto-generated method stub
		mySQLiteOpenHelper = new MySQLiteOpenHelper(MainActivity.this);
	}
    private float setFormat(double paramDouble)
    {
      return (float)new BigDecimal(paramDouble).setScale(2, 4).doubleValue();
    }
    
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		this.y = event.values[1];
		int i = (int)this.y;
		if(state==0){
			t1=i;
		}else if(state==1){
			s1=90+t1;
			t2=i;
		}else if(state==2){
			
			t3=i;
		}
		while (true)
	    {
		
		 this.mToast.setText(t1+" "+s1+" "+t2+" "+s2+ "\n\n");
		 if (hasWindowFocus())
	       this.mToast.show();
	      return;
	    }
	}

	public double SIN(int arg){
		return Math.sin(3.141592653589793D * arg / 180.0D);	
	}
	public double COS(int arg){
		return Math.cos(3.141592653589793D * arg / 180.0D);	
	}
	public double TAN(int arg){
		return Math.tan(3.141592653589793D * arg / 180.0D);	
	}
	public double COT(int arg){
		return 1/Math.tan(3.141592653589793D * arg / 180.0D);	
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
		// TODO Auto-generated method stub
		try
	    {
	      this.myCamera = Camera.open();
	      Display localDisplay = ((WindowManager)getSystemService("window")).getDefaultDisplay();
	      this.myCamera.getParameters().setPreviewSize(localDisplay.getWidth(), localDisplay.getHeight());
	      this.myCamera.setDisplayOrientation(90);
	      this.myCamera.setPreviewDisplay(paramSurfaceHolder);
	      return;
	    }
	    catch (Exception localException)
	    {
	    }
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {//当surface发生任何结构性的变化时（格式或者大小），该方法就会被立即调用
		// TODO Auto-generated method stub
		Camera.Parameters localParameters = this.myCamera.getParameters();
	    this.myCamera.setParameters(localParameters);
	    this.myCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		this.myCamera.setPreviewCallback(null);
	    this.myCamera.stopPreview();
	    this.myCamera.release();
	    this.myCamera = null;
	}

	@Override
	public void onClick(View paramView) {
		// TODO Auto-generated method stub
		switch (paramView.getId()){
		default:	      return;
		case R.id.settingbtn:
			//this.mToast.setText(result+" "+t1+" "+s3+" "+t2+" "+s1+" "+t3+"  "+s2+" " + "\n\n");
			//state=1;
			makeDialog();
			return;
		case R.id.top://确定顶部
			state=2;
			s2=-90-t2;
			double high=Cacular();
			mySQLiteOpenHelper.edit(result,high);
			Intent intent=new Intent(MainActivity.this, dblook.class);
			startActivity(intent);
			return;
		case R.id.lockbuttombtn://确定底部
			state=1;
			
			return;
		}
	}
	public double Cacular(){
		double H=0.0D;
		H=L+L/TAN(s1)*TAN(s2);
		return H;
	}
	private void makeDialog(){
		this.mToast.cancel();
		View localView = LayoutInflater.from(this).inflate(R.layout.add,null);
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(R.string.action_settings);
		this.mInputTv =((TextView)localView.findViewById(R.id.inputTv));
		this.mInputTv.setText(R.string.input_phone_height);
		this.mInputHeightET = ((EditText)localView.findViewById(R.id.heightEdit));
		this.mInputHeightET.setText(MainActivity.this.L+"");
		while (true)
	    {
			localBuilder.setView(localView);
			localBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String str = MainActivity.this.mInputHeightET.getText().toString();
					double d = Double.valueOf(str).doubleValue();
					MainActivity.this.L = MainActivity.this.setFormat(d);
				}
			});
			localBuilder.setNegativeButton(R.string.cancel, null);
			localBuilder.show();
			return;
	    }
	}
}
