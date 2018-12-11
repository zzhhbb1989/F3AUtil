package wheelpicker.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 省份
 * <br/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016-10-15 19:06
 * Builder:Android Studio
 */
public class Province extends Area implements LinkageFirst<City> {
    
    private List<City> cities = new ArrayList<>();
    
    public Province() {
        super();
    }

    public Province(String areaName) {
        super(areaName);
    }

    public Province(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public List<City> getCities() {
        return cities;
    }
    
    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public List<City> getSeconds() {
        return cities;
    }
    
    ////////////////////// 省市区 ///////////////////////
    
    public void setProvince_id(String id) {
        setAreaId(id);
        initProvince_id();
    }
    
    public void setProvince_name(String name) {
        setAreaName(name);
    }
    
    public void setCity(List<City> city) {
        setCities(city);
        initProvince_id();
    }
    
    private void initProvince_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && cities != null && !cities.isEmpty() &&
                TextUtils.isEmpty(cities.get(0).getProvinceId())) {
            for (City city : cities) {
                city.setProvinceId(areaId);
            }
        }
    }
    
    ////////////////////// 资质等级 ///////////////////////
    
    public void setHonor_id(String id) {
        setAreaId(id);
        initHonor_id();
    }
    
    public void setHonor_name(String name) {
        setAreaName(name);
    }
    
    public void setList_major(List<City> city) {
        setCities(city);
        initHonor_id();
    }
    
    private void initHonor_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && cities != null && !cities.isEmpty() &&
                TextUtils.isEmpty(cities.get(0).getProvinceId())) {
            for (City city : cities) {
                city.setProvinceId(areaId);
            }
        }
    }

    //////////////////////// 人才技能下拉列表////////////////////////
     public void setP_id(String id) {
        setAreaId(id);
        initP_id();
    }

    public void setP_name(String name) {
        setAreaName(name);
    }


    public void setSkill(List<City> city) {
        setCities(city);
        initP_id();
    }

    private void initP_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && cities != null && !cities.isEmpty() &&
                TextUtils.isEmpty(cities.get(0).getProvinceId())) {
            for (City city : cities) {
                city.setProvinceId(areaId);
            }
        }
    }
    /////////////////////////证书等级////////////////////
     public void setCertificate_type_id(String id) {
        setAreaId(id);
         initCertificate_type_id();
    }

    public void setCertificate_type_name(String name) {
        setAreaName(name);
    }


    public void setClass(List<City> city) {
        setCities(city);
        initCertificate_type_id();
    }

    private void initCertificate_type_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && cities != null && !cities.isEmpty() &&
                TextUtils.isEmpty(cities.get(0).getProvinceId())) {
            for (City city : cities) {
                city.setProvinceId(areaId);
            }
        }
    }


}