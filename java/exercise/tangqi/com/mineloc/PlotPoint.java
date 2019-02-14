package exercise.tangqi.com.mineloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.amap.api.fence.PoiItem;
import com.amap.api.maps.AMap;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.List;

public class PlotPoint extends AppCompatActivity {
    private MapView mapview;
    private AMap aMap;
    public Double[][] data = new Double[1000][2];
    public Double jingdu;
    public Double weidu;
    private List<PoiItem> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotpoint);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        data = (Double[][]) bundle.getSerializable("jingweidu");
        mapview = (MapView) findViewById(R.id.map_plot);
        mapview.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapview.getMap();
            mapview.setSelected(true);
        }
        addpoint();
    }

    private void addpoint() {
        int i = 0;
        int j;
        while (data[i][0] != null) {
            i++;
        }
        for (j = 0; j < i; j++) {
            jingdu = data[j][0];
            weidu = data[j][1];
            LatLng latLng = new LatLng(weidu, jingdu);
            String jingdustr=jingdu.toString();
            String weidustr=weidu.toString();
            Marker marker = aMap.addMarker(new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.bomb)).
                    title("当前位置").
                    position(latLng).
                    snippet("经度："+jingdustr+"\n"+"纬度："+weidustr));
        }
        Toast.makeText(this,"共有"+j+"组数据",Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng(weidu, jingdu);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));//设置中心点
        aMap.moveCamera(CameraUpdateFactory.zoomTo(8)); // 设置地图可视缩放大小
    }

}
