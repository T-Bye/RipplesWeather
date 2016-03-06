package com.biner.ripplesweather.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.biner.ripplesweather.R;
import com.biner.ripplesweather.model.City;
import com.biner.ripplesweather.model.TodayWeather;
import com.biner.ripplesweather.util.DBUtil;
import com.biner.ripplesweather.util.HttpUtil;
import com.biner.ripplesweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<TodayWeather> citySelected = new ArrayList<>();
    public LocationClient mLocationClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (DBUtil.queryTodayWeather().size() == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
            builder.setMessage("是否进行定位？");
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
                    initLocation();
                    mLocationClient.registerLocationListener(new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation location) {
                            String cityName = null;
                            StringBuffer sb = new StringBuffer(256);
                            sb.append("time : ");
                            sb.append(location.getTime());
                            sb.append("\nerror code : ");
                            sb.append(location.getLocType());
                            sb.append("\nlatitude : ");
                            sb.append(location.getLatitude());
                            sb.append("\nlontitude : ");
                            sb.append(location.getLongitude());
                            sb.append("\nradius : ");
                            sb.append(location.getRadius());
                            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                                sb.append("\nspeed : ");
                                sb.append(location.getSpeed());// 单位：公里每小时
                                sb.append("\nsatellite : ");
                                sb.append(location.getSatelliteNumber());
                                sb.append("\nheight : ");
                                sb.append(location.getAltitude());// 单位：米
                                sb.append("\ndirection : ");
                                sb.append(location.getDirection());// 单位度
                                sb.append("\naddr : ");
                                sb.append(location.getAddrStr());
                                cityName = location.getAddrStr();
                                sb.append("\ndescribe : ");
                                sb.append("gps定位成功");

                            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                                sb.append("\naddr : ");
                                sb.append(location.getAddrStr());
                                cityName = location.getAddrStr();
                                //运营商信息
                                sb.append("\noperationers : ");
                                sb.append(location.getOperators());
                                sb.append("\ndescribe : ");
                                sb.append("网络定位成功");
                            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                                sb.append("\ndescribe : ");
                                sb.append("离线定位成功，离线定位结果也是有效的");
                            } else if (location.getLocType() == BDLocation.TypeServerError) {
                                sb.append("\ndescribe : ");
                                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                                sb.append("\ndescribe : ");
                                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                                sb.append("\ndescribe : ");
                                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                            }
                            if (cityName != null) {
                                String[] s1 = cityName.split("市");
                                String[] s2 = s1[0].split("省");
                                List<City> cityList = DBUtil.loadCity();
                                for (City city : cityList) {
                                    if (s2[1].equals(city.getCityName())) {
                                        final String Id = city.getId();
                                        //存数据
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String address = "https://api.heweather.com/x3/weather?cityid=" + Id + "&key=b2ad4353d8eb4e048c438de6666f1153";
                                                String JsonResult = HttpUtil.sendHttpRequest(address);
                                                Utility.jsonWeatherResponse(JsonResult);
                                                mLocationClient.stop();
                                            }
                                        }).start();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "定位失败，请点击右上角进行添加！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "定位失败，请点击右上角进行添加！", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("BaiduLocationApiDem", sb.toString());
                            Log.e("cityName", cityName);
                        }
                    });
                    mLocationClient.start();
                }
            });
            builder.create().show();
        }

        citySelected = DBUtil.queryTodayWeather();

        final Intent intent = getIntent();
        final String currentPage;
        currentPage = intent.getStringExtra("CITYID");
        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(sectionsPagerAdapter);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(WeatherActivity.this, "长按删除城市！", Toast.LENGTH_SHORT).show();
                final ListView lvmycare = (ListView) findViewById(R.id.lvmycare);
                final List<String> dataList = new ArrayList<>();
                if (citySelected.size() > 0) {
                    dataList.clear();
                    for (int i = 0; i < citySelected.size(); i++) {
                        dataList.add(citySelected.get(i).getCityName());
                    }
                } else {
                    dataList.clear();
                    lvmycare.setClickable(false);
                    dataList.add("欢迎使用涟漪天气！");
                    dataList.add("点击添加按钮添加!");
                    dataList.add("让我们开始使用吧！");
                }
                final ArrayAdapter<String> cityAddAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, dataList);
                lvmycare.setAdapter(cityAddAdapter);
                lvmycare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        viewPager.setCurrentItem(position);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                });
                if (citySelected.size() > 0) {

                    lvmycare.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            DBUtil.deleteCity(citySelected.get(position).getCityId());
                            citySelected = DBUtil.queryTodayWeather();
                            if (citySelected.size() > 0) {
                                dataList.clear();
                                for (int i = 0; i < citySelected.size(); i++) {
                                    dataList.add(citySelected.get(i).getCityName());
                                }

                            } else {
                                dataList.clear();
                                lvmycare.setClickable(false);
                                dataList.add("欢迎使用涟漪天气！");
                                dataList.add("点击下方添加按钮!");
                                dataList.add("让我们开始使用吧！");
                            }
                            cityAddAdapter.notifyDataSetChanged();
                            sectionsPagerAdapter.notifyDataSetChanged();
                            drawer.closeDrawer(GravityCompat.START);
                            return true;
                        }
                    });
                }

            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (currentPage != null) {
            for (int i = 0; i < citySelected.size(); i++) {
                if (currentPage.equals(citySelected.get(i).getCityId())) {
                    viewPager.setCurrentItem(i);
                }
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("CITYID", citySelected.get(position).getCityId());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return citySelected.size();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(WeatherActivity.this, CityChooseActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getTitle()));
        } else if (id == R.id.nav_about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


//        cityAddAdapter.notifyDataSetChanged();
//        lvmycare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
}
