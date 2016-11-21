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
     * 取得可读权限的LancherInfoDao
     *
     * @return LancherInfoDao
     */
    public LancherInfoDao getWriteableLancherInfo() {
        return getReadableDaoSession().getLancherInfoDao();
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

    public void insertLancherInfo(LancherInfo lancherInfo) {
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        lancherInfoDao.insert(lancherInfo);
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

    public void insertLancherInfoList(List<LancherInfo> lancherInfos) {
        if (lancherInfos == null || lancherInfos.isEmpty()) {
            return;
        }
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        lancherInfoDao.insertInTx(lancherInfos);
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

    public void deleteLancherInfo(LancherInfo lancherInfo){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        lancherInfoDao.delete(lancherInfo);
    }
    /**
     * 根据id删除一条记录
     *
     * @param id
     */
    public void deleteAppInfo(Long id){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.deleteByKey(id);
    }

    public void deleteLancherInfo(Long id){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        lancherInfoDao.deleteByKey(id);
    }
    /**
     * 删除所有记录
     *
     */
    public void deleteAllAppInfo(){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.deleteAll();
    }

    public void deleteAllLancherInfo(){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        lancherInfoDao.deleteAll();
    }
    /**
     * 查询获取一条数据
     *
     */
    public List<AppInfo> queryAppInfo(Long id){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        QueryBuilder<AppInfo> qb = appInfoDao.queryBuilder();
        qb.where(AppInfoDao.Properties.Id.eq(id));
        return qb.list();
    }

    public List<LancherInfo> queryLancherInfo(Long id){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        QueryBuilder<LancherInfo> qb = lancherInfoDao.queryBuilder();
        qb.where(LancherInfoDao.Properties.Id.eq(id));
        return qb.list();
    }
    /**
     * 查询获取数据列表
     *
     */
    public List<AppInfo> queryAppInfoList(){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        QueryBuilder<AppInfo> qb = appInfoDao.queryBuilder();
        return qb.list();
    }

    public List<LancherInfo> querylancherInfoList(){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        QueryBuilder<LancherInfo> qb = lancherInfoDao.queryBuilder();
        return qb.list();
    }
    /**
     * 根据页数查询数据列表
     *
     */
    public List<AppInfo> queryAppInfoListByPage(int page){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        QueryBuilder<AppInfo> qb = appInfoDao.queryBuilder();
        qb.where(AppInfoDao.Properties.Page.eq(page)).orderAsc(AppInfoDao.Properties.PagePos);
        return qb.list();
    }

    /**
     *根据位置查找Lancherinfo
     */
    public List<LancherInfo> queryLancherInfoByPos(int pos,String panel){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        QueryBuilder<LancherInfo> qb = lancherInfoDao.queryBuilder();
        qb.where(LancherInfoDao.Properties.Position.eq(pos)
        ,LancherInfoDao.Properties.Page.eq(panel));

        return qb.list();
    }
    /**
     *根据页数查找Lancherinfo
     */
    public List<LancherInfo> queryLancherInfoByPage(String page){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        QueryBuilder<LancherInfo> qb = lancherInfoDao.queryBuilder();
        qb.where(LancherInfoDao.Properties.Page.eq(page)).orderAsc(LancherInfoDao.Properties.Page);
        return qb.list();
    }

    /**
     * 更新数据列表
     */
    public void updateAppInfo(AppInfo appInfo){
        AppInfoDao appInfoDao = getWriteableAppInfoDao();
        appInfoDao.update(appInfo);
    }

    public void updateLancherInfo(LancherInfo lancherInfo){
        LancherInfoDao lancherInfoDao = getWriteableLancherInfo();
        lancherInfoDao.update(lancherInfo);
    }
}
