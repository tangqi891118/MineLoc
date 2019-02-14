package exercise.tangqi.com.mineloc;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.io.File;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity {
    private Button btn = null;
    private Button btn2 = null;
    private Button btn3 = null;
    private Button btn4 = null;
    private Button btn5 = null;
    private EditText edittext1 = null;
    private EditText edittext2 = null;
    private double jingdu = 0;
    private double weidu = 0;
    private String str;
    private String str2;
    private MapView mapview = null;
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";//
    public Double[][] data = new Double[1000][2];
    private int i = 0;
    private int j = 0;
    private static final int PERMISSON_REQUESTCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        edittext1 = (EditText) findViewById(R.id.editText1);
        edittext2 = (EditText) findViewById(R.id.editText2);
        mapview = (MapView) findViewById(R.id.map);
        mapview.onCreate(savedInstanceState);
        AMap aMap = null;
        if (aMap == null) {
            aMap = mapview.getMap();
        }
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        myLocationStyle.showMyLocation(true);
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},
                    PERMISSON_REQUESTCODE);//自定义的code
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = edittext1.getText().toString();
                str2 = edittext2.getText().toString();
                if (str.length() == 0 && str2.length() != 0) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入经度")
                            .setIcon(R.mipmap.aiapps_empty_icon_error)
                            .create();
                    alertDialog1.show();
                } else if (str2.length() == 0 && str.length() != 0) {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入纬度")
                            .setIcon(R.mipmap.aiapps_empty_icon_error)
                            .create();
                    alertDialog2.show();
                } else if (str2.length() == 0 && str.length() == 0) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("请输入经度和纬度")
                            .setIcon(R.mipmap.aiapps_empty_icon_error)
                            .create();
                    alertDialog1.show();
                } else {
                    jingdu = Double.parseDouble(str);//经度转换成double型
                    weidu = Double.parseDouble(str2);//纬度转换成double型\
                }
                AMap aMap = mapview.getMap();
                if (str.length() != 0 && str2.length() != 0) {
                    aMap.showIndoorMap(true);
                    LatLng latLng = new LatLng(weidu, jingdu);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));//设置中心点
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(8)); // 设置地图可视缩放大小
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bomb));
                    markerOptions.position(latLng);
                    aMap.addMarker(markerOptions);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                }
            }
        });
//保存当前点
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                str = edittext1.getText().toString();
                str2 = edittext2.getText().toString();
                if (str2.length() != 0 && str.length() != 0) {
                    insertData();
                }
            }
        });
        //清空地图和数据库
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
        //绘制分布图
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(data[0][0]!=null) {
                   Intent intent = new Intent();
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("jingweidu", data);
                   intent.putExtras(bundle);
                   intent.setClass(MainActivity.this, PlotPoint.class);
                   startActivity(intent);
               }
               else {
                   Toast.makeText(MainActivity.this,"数据库为空",Toast.LENGTH_SHORT).show();
               }
            }
        });
        //到那儿去
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = edittext1.getText().toString();
                str2 = edittext2.getText().toString();
                if (str2.length() != 0 && str.length() != 0) {
                    openGaoDeNavi(PN_GAODE_MAP);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapview.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapview.onSaveInstanceState(outState);
    }

    public void insertData() {
        data[i][0] = jingdu;
        data[i][1] = weidu;
        i++;
        Toast.makeText(MainActivity.this, "已保存:" + i + "组数据", Toast.LENGTH_SHORT).show();
    }

    public void deleteData() {
        AMap aMap = mapview.getMap();
        aMap.clear();
        str = null;
        str2 = null;
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        myLocationStyle.showMyLocation(true);

        for (j = 0; j < i; j++) {
            data[j][0] = null;
            data[j][0] = null;
        }
        Toast.makeText(MainActivity.this, "已删除:" + j + "组数据", Toast.LENGTH_SHORT).show();
        i = 0;
        j = 0;
    }

    private void openGaoDeNavi(String packagename) {
        if (isInstallByread(packagename)) {
            StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=")
                    .append("yitu8_driver").append("&lat=").append(weidu)
                    .append("&lon=").append(jingdu)
                    .append("&dev=").append(1)
                    .append("&style=").append(0);
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(stringBuffer.toString()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } else {
            Toast.makeText(this, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private boolean isInstallByread(String packagename) {
        return new File("/data/data/" + packagename).exists();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            isExit.setTitle("确定退出？");
            isExit.setMessage("确定要退出吗？");
            isExit.setIcon(R.mipmap.aiapps_empty_icon_error);
            isExit.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            isExit.show();
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);//可在此继续其他操作。
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                AMap aMap = null;
                if (aMap == null) {
                    aMap = mapview.getMap();
                }
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                MyLocationStyle myLocationStyle = new MyLocationStyle();
                aMap.setMyLocationStyle(myLocationStyle);
                aMap.setMyLocationEnabled(true);
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                aMap.setMyLocationStyle(myLocationStyle);
                myLocationStyle.showMyLocation(true);
            } else {
                // Permission Denied
            }
        }
    }


}
