package cn.abcdsxg.app.appJump.Data.greenDao;

import android.content.ContentValues;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.sql.Blob;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 16-11-16 10:46
 */
@Entity
public class LancherInfo {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "title")
    private String title;
    @Property(nameInDb = "intent")
    private String intent;
    @Property(nameInDb = "pkgName")
    private String pkgName;
    @Property(nameInDb = "itemType")
    private int itemType;
    @Property(nameInDb = "iconType")
    private int iconType;
    @Property(nameInDb = "iconPkg")
    private String iconPkg;
    @Property(nameInDb = "iconRes")
    private String iconRes;
    @Property(nameInDb = "iconData")
    private byte[] iconData;
    @Property(nameInDb = "uri")
    private String uri;
    @Property(nameInDb = "flag")
    private int flag;
    @Property(nameInDb = "modDate")
    private long modDate;
    @Property(nameInDb = "position")
    private int position;
    @Property(nameInDb = "page")
    private String page;
    public String getPage() {
        return this.page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public long getModDate() {
        return this.modDate;
    }
    public void setModDate(long modDate) {
        this.modDate = modDate;
    }
    public int getFlag() {
        return this.flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getUri() {
        return this.uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public byte[] getIconData() {
        return this.iconData;
    }
    public void setIconData(byte[] iconData) {
        this.iconData = iconData;
    }
    public String getIconRes() {
        return this.iconRes;
    }
    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }
    public String getIconPkg() {
        return this.iconPkg;
    }
    public void setIconPkg(String iconPkg) {
        this.iconPkg = iconPkg;
    }
    public int getIconType() {
        return this.iconType;
    }
    public void setIconType(int iconType) {
        this.iconType = iconType;
    }
    public int getItemType() {
        return this.itemType;
    }
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
    public String getPkgName() {
        return this.pkgName;
    }
    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
    public String getIntent() {
        return this.intent;
    }
    public void setIntent(String intent) {
        this.intent = intent;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1678216117)
    public LancherInfo(Long id, String title, String intent, String pkgName, int itemType,
            int iconType, String iconPkg, String iconRes, byte[] iconData, String uri, int flag,
            long modDate, int position, String page) {
        this.id = id;
        this.title = title;
        this.intent = intent;
        this.pkgName = pkgName;
        this.itemType = itemType;
        this.iconType = iconType;
        this.iconPkg = iconPkg;
        this.iconRes = iconRes;
        this.iconData = iconData;
        this.uri = uri;
        this.flag = flag;
        this.modDate = modDate;
        this.position = position;
        this.page = page;
    }
    @Generated(hash = 328424281)
    public LancherInfo() {
    }

//    public LancherInfo(Long id,String title, String intent, String pkgName,
//                       int itemType, int iconType, String iconPkg, String iconRes,
//                       byte[] iconData,int flag ,String uri, long modDate,int position,String page) {
//        this.id = id;
//        this.title = title;
//        this.intent = intent;
//        this.pkgName = pkgName;
//        this.itemType = itemType;
//        this.iconType = iconType;
//        this.iconPkg = iconPkg;
//        this.iconRes = iconRes;
//        this.iconData=iconData;
//        this.flag=flag;
//        this.uri = uri;
//        this.modDate = modDate;
//        this.position = position;
//        this.page = page;
//    }


}
