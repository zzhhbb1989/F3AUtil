package wheelpicker.entity;

/**
 * 区县
 * <br/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016-10-15 19:08
 * Builder:Android Studio
 */
public class County extends Area implements LinkageThird {
    private String cityId;

    public County() {
        super();
    }

    public County(String areaName) {
        super(areaName);
    }

    public County(String areaId, String areaName) {
        super(areaId, areaName);
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    
    ////////////////////// 省市区 ///////////////////////
    
    public void setCounty_id(String id) {
        setAreaId(id);
    }
    
    public void setCounty_name(String name) {
        setAreaName(name);
    }
    
    ////////////////////// 资质等级 ///////////////////////
    
    public void setRank_id(String id) {
        setAreaId(id);
    }
    
    public void setRank_name(String name) {
        setAreaName(name);
    }

}
