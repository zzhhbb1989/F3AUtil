package wheelpicker.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 地市
 * <br/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016-10-15 19:07
 * Builder:Android Studio
 */
public class City extends Area implements LinkageSecond<County> {
    private String provinceId;
    private List<County> counties = new ArrayList<>();

    public City() {
        super();
    }

    public City(String areaName) {
        super(areaName);
    }

    public City(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }

    @Override
    public List<County> getThirds() {
        return counties;
    }
    
    ////////////////////// 省市区 ///////////////////////
    
    public void setCity_id(String id) {
        setAreaId(id);
        initCity_id();
    }
    
    public void setCity_name(String name) {
        setAreaName(name);
    }
    
    public void setArea(List<County> county) {
        setCounties(county);
        initCity_id();
    }
    
    private void initCity_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && counties != null && !counties.isEmpty() &&
                TextUtils.isEmpty(counties.get(0).getCityId())) {
            for (County county : counties) {
                county.setCityId(getAreaId());
            }
        }
    }
    
    ////////////////////// 资质等级 ///////////////////////
    
    public void setMajor_id(String id) {
        setAreaId(id);
        initMajor_id();
    }
    
    public void setMajor_name(String name) {
        setAreaName(name);
    }
    
    public void setList_rank(List<County> county) {
        setCounties(county);
        initMajor_id();
    }
    
    private void initMajor_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && counties != null && !counties.isEmpty() &&
                TextUtils.isEmpty(counties.get(0).getCityId())) {
            for (County county : counties) {
                county.setCityId(getAreaId());
            }
        }
    }
    ////////////////////// 技能 ///////////////////////

    public void setSkill_id(String id) {
        setAreaId(id);
        initSkill_id();
    }

    public void setSkill_name(String name) {
        setAreaName(name);
    }

    private void initSkill_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && counties != null && !counties.isEmpty() &&
                TextUtils.isEmpty(counties.get(0).getCityId())) {
            for (County county : counties) {
                county.setCityId(getAreaId());
            }
        }
    }
    ////////////////////// 证书 ///////////////////////

    public void setCertificate_rank_id(String id) {
        setAreaId(id);
        initCertificate_rank_id();
    }

    public void setCertificate_rank_name(String name) {
        setAreaName(name);
    }

    private void initCertificate_rank_id() {
        String areaId = getAreaId();
        if (!TextUtils.isEmpty(areaId) && counties != null && !counties.isEmpty() &&
                TextUtils.isEmpty(counties.get(0).getCityId())) {
            for (County county : counties) {
                county.setCityId(getAreaId());
            }
        }
    }

}