package cn.abcdsxg.app.appJump.Data.Utils;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.abcdsxg.app.appJump.Data.AppInfo;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 2016/7/25 10:23
 */
public class SuUtils {
    private static String command=null;

    public static boolean isRoot(){
        DataOutputStream outputStream=null;
        Process process = null;
        try {
            //获取root权限并执行command命令
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write("echo test".getBytes());
            outputStream.writeBytes("\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            int exitValue= process.waitFor();
            //判断获取root是否成功
            if(exitValue==0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) { outputStream.close();   }
                if (process!=null       ) { process.destroy();      }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //执行启动指定app的命令并返回结果
    public static boolean startApp(String packageName,String className,String extra){
        command = "am start -n " + packageName + "/" + className;
        Process process = null;
        DataOutputStream outputStream=null;
        try {
            //获取root权限并执行command命令
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write(command.getBytes());
            outputStream.writeBytes("\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            int exitValue= process.waitFor();
            //判断获取root是否成功
            if(exitValue==0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) { outputStream.close();   }
                if (process!=null       ) { process.destroy();      }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static AppInfo getAppInfo(){
        command = "dumpsys activity | grep mFocusedActivity";
        Process process = null;
        DataOutputStream outputStream=null;
        DataInputStream inPutStrem=null;
        try {
            //获取root权限并执行command命令
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            inPutStrem=new DataInputStream(process.getInputStream());
            outputStream.write(command.getBytes());
            outputStream.writeBytes("\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            //获取执行后的输出信息
            String line = null;
            String value="";
            while ((line = inPutStrem.readLine()) != null) {
                value += line;
            }
            //判断获取root是否成功
            int exitValue= process.waitFor();
            if(exitValue==0){
                //返回正则之后的AppInfo
                return patternValue(value);
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) { outputStream.close();   }
                if (process!=null       ) { process.destroy();      }
                if(inPutStrem!=null     ) { inPutStrem.close();     }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //正则表达式获取正在前台界面的类名和包名
    private static AppInfo patternValue(String value) {
        String patternString="mFocusedActivity: ActivityRecord\\{([\\s\\S]*) ([\\s\\S]*)/.([\\s\\S]*) ([\\s\\S]*)";
        Pattern pattern=Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(value);
        if(matcher.find()){
            String pkgName=matcher.group(2);
            String clsName=pkgName+"."+matcher.group(3);
            AppInfo appInfo=new AppInfo(pkgName,clsName);
            return appInfo;
        }else{
            return null;
        }

    }


}
