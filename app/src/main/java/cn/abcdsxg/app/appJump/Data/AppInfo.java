package cn.abcdsxg.app.appJump.Data;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
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
    private long id;
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

    public AppInfo(String pkgName, String clsName, String appName) {
        this.pkgName = pkgName;
        this.clsName = clsName;
        AppName = appName;
    }

    @Generated(hash = 695700962)
    public AppInfo(long id, String pkgName, String clsName, String AppName) {
        this.id = id;
        this.pkgName = pkgName;
        this.clsName = clsName;
        this.AppName = AppName;
    }

    @Generated(hash = 1656151854)
    public AppInfo() {
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "id='" + id + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", clsName='" + clsName + '\'' +
                ", AppName='" + AppName + '\'' +
                '}';
    }
}
