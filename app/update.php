<?php
$ver=$_GET["version"];
$pkg=$_GET["pkg"];
if(!isset($ver) || !isset($pkg)){


}

switch ($pkg) {
	case 'cn.abcdsxg.app.syncsend':
		if(floatval($ver)<1.4){
		    print_r( 
		    	json_encode(
		    	array(
		    	"msg"=>"更新内容:\n 1、去掉了豆瓣增加了line平台\n2、修改了google play版和国内版不同的捐赠方式",
		    	"url"=>"http://dl.abcdsxg.cn/app/syncsend-release-v1.4.apk"
		    )));
		}
		else {
			print_r( 
				json_encode(
				array(
				"msg"=>"none",
				"url"=>""
			))); 
		}
		break;
	case 'cn.abcdsxg.app.appJump':
		if(floatval($ver)<2.0){
		    print_r( 
		    	json_encode(
		    	array(
		    	"msg"=>"重大更新:\n1、.新增滑动启动功能，需要悬浮窗权限！！\n2、优化了检测类名包名服务，锁屏时会自动停止\n3、修改图标和应用名字，欢迎提供更好的想法～～",
		    	"url"=>"http://dl.abcdsxg.cn/app/app-googlePlay-release-2.0.apk"
		    )));
		}
		else {
			print_r( 
				json_encode(
				array(
				"msg"=>"none",
				"url"=>""
			))); 
		}
		break;
	case 'cn.abcdsxg.app.joke':
		if(floatval($ver)<1.2){
		    print_r( 
		    	json_encode(
		    	array(
		    	"msg"=>"更新内容:\n1、修复了已知bug。\n2、去除同一分类下的重复数据",
		    	"url"=>"https://www.abcdsxg.cn/app/google/joke/app-abcdsxg-release-1.2.apk"
		    )));
		}
		else {
			print_r( 
				json_encode(
				array(
				"msg"=>"none",
				"url"=>""
			))); 
		}
		break;
	
	default:

		break;
}




?>
