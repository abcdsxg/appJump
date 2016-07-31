package cn.abcdsxg.app.appJump.Data.greenDao;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import cn.abcdsxg.app.appJump.Base.BaseApplication;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/27 09:47
 */
public class DBManager {
    private final static String dbName = AppInfoDao.TABLENAME;
    private static DBManager mInstance;
    private static DaoMaster daoMaster;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager() {
        this.context = BaseApplication.getInstance();
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *@return DBManager
     */
    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }
    /**
     * 取得可读权限的DaoMaster
     *
     * @return daoMaster
     */
    public DaoMaster getReadableDaoMaster() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        daoMaster = new DaoMaster(openHelper.getReadableDatabase());
        return daoMaster;
    }
    /**
     * 取得可写权限的DaoMaster
     * @return DaoMaster
     */
    private DaoMaster getWritableDaoMaster() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        return daoMaster;
    }
    /**
     * 取得可读权限的DaoSession
     *
     * @return DaoSession
     */
    public DaoSession getReadableDaoSession() {
        return getReadableDaoMaster().newSession();
    }
    /**
     * 取得可读权限的DaoSession
     *
     * @return DaoSession
     */
    public DaoSession getWriteableDaoSession() {
        return getWritableDaoMaster().newSession();
    }
    /**
     * 取得可读权限的AppInfoDao
     *
     * @return AppInfoDao
     */
    public AppInfoDao getReadableAppInfoDao() {
        return getReadableDaoSession().getAppInfoDao();
    }
    /**
     * 取得可读权限的AppInfoDao
     *
     * @return AppInfoDao
     */
    public AppInfoDao getWriteableAppInfoDao() {
        return getWriteableDaoSession().getAppInfoDao();
    }
    /**
     * 插入一条记录
     *
     * @param appInfo
     */
    public void insertAppInfo(AppInfo appInfo) {
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.insert(appInfo);
    }

    /**
     * 插入用户集合
     *
     * @param appInfos
     */
    public void insertAppInfoList(List<AppInfo> appInfos) {
        if (appInfos == null || appInfos.isEmpty()) {
            return;
        }
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.insertInTx(appInfos);
    }
    /**
     * 删除一条记录
     *
     * @param appInfo
     */
    public void deleteAppInfo(AppInfo appInfo){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.delete(appInfo);
    }
    /**
     * 删除所有记录
     *
     */
    public void deleteAllAppInfo(){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.deleteAll();
    }
    /**
     * 查询获取一条数据
     *
     */
    public List<AppInfo> queryAppInfo(Long id){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        QueryBuilder<AppInfo> qb = appInfoDao.queryBuilder();
        qb.where(AppInfoDao.Properties.Id.eq(id));
        List<AppInfo> list = qb.list();
        return list;
    }
    /**
     * 查询获取数据列表
     *
     */
    public List<AppInfo> queryAppInfoList(){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        QueryBuilder<AppInfo> qb = appInfoDao.queryBuilder();
        List<AppInfo> list = qb.list();
        return list;
    }
    /**
     * 根据页数查询数据列表
     *
     */
    public List<AppInfo> queryAppInfoListByPage(int page){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        QueryBuilder<AppInfo> qb = appInfoDao.queryBuilder();
        qb.where(AppInfoDao.Properties.Page.eq(page)).orderAsc(AppInfoDao.Properties.PagePos);
        List<AppInfo> list = qb.list();
        return list;
    }
    /**
     * 更新数据列表
     *
     * @param appInfo
     */
    public void updateAppInfo(AppInfo appInfo){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.update(appInfo);
    }
}
