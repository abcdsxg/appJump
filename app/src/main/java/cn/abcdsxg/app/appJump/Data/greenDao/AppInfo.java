package cn.abcdsxg.app.appJump.Data.greenDao;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/25 22:29
 */
@Entity
public class AppInfo {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "page")
    private int page;
    @Property(nameInDb = "pagePos")
    private int pagePos;
    @Property(nameInDb = "pkgName")
    private String pkgName;
    @Property(nameInDb = "clsName")
    private String clsName;
    @Property(nameInDb = "AppName")
    private String AppName;

    public AppInfo(String pkgName, String clsName) {
        this.pkgName = pkgName;
        this.clsName = clsName;
    }

    public AppInfo(String pkgName, String clsName, String AppName, int page ,int pagePos) {
        this.pkgName = pkgName;
        this.clsName = clsName;
        this.AppName = AppName;
        this.page=page;
        this.pagePos=pagePos;
    }

    public String getAppName() {
        return this.AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getClsName() {
        return this.clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getPagePos() {
        return this.pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 716346491)
    public AppInfo(Long id, int page, int pagePos, String pkgName, String clsName,
            String AppName) {
        this.id = id;
        this.page = page;
        this.pagePos = pagePos;
        this.pkgName = pkgName;
        this.clsName = clsName;
        this.AppName = AppName;
    }

    @Generated(hash = 1656151854)
    public AppInfo() {
    }


}
