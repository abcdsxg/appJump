package cn.abcdsxg.app.appJump.Data.greenDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

import cn.abcdsxg.app.appJump.Data.AppInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig appInfoDaoConfig;

    private final AppInfoDao appInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        appInfoDaoConfig = daoConfigMap.get(AppInfoDao.class).clone();
        appInfoDaoConfig.initIdentityScope(type);

        appInfoDao = new AppInfoDao(appInfoDaoConfig, this);

        registerDao(AppInfo.class, appInfoDao);
    }
    
    public void clear() {
        appInfoDaoConfig.getIdentityScope().clear();
    }

    public AppInfoDao getAppInfoDao() {
        return appInfoDao;
    }

}
