package cn.abcdsxg.app.appJump.View;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.abcdsxg.app.appJump.Data.Adapter.PanelItemAdapter;
import cn.abcdsxg.app.appJump.Data.Constant;
import cn.abcdsxg.app.appJump.Data.Utils.SpUtil;
import cn.abcdsxg.app.appJump.Data.Utils.ToolUtils;
import cn.abcdsxg.app.appJump.Data.greenDao.DBManager;
import cn.abcdsxg.app.appJump.Data.greenDao.LancherInfo;
import cn.abcdsxg.app.appJump.R;

/**
 * Author : 时小光
 * Email  : abcdsxg@gmail.com
 * Blog   : http://www.abcdsxg.cn
 * Date   : 16-11-15 14:43
 */
public class TouchToSelectView extends LinearLayout {
    private static final String TAG = "TouchToSelectView";
    private int mCurrentX,mCurrentY;

    private static final int arcRadiu=150;

    //是否改变符合条件时外圈的颜色和子view的位置
    private boolean isChangeColor;
    private int pos;

    private GridView gridView;

    private List<LancherInfo> lancherInfos;

    private int[] colors=new int[]{Color.rgb(61,201,204),
            Color.rgb(253, 241, 150),
            Color.rgb(199, 39, 103),
            Color.rgb(244, 124, 124),
            Color.rgb(247, 56, 89),
            Color.rgb(194, 252, 217)};


    public TouchToSelectView(Context context) {
        this(context,null);
    }

    public TouchToSelectView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchToSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int chindCount=getChildCount();
        for (int j = 0; j < chindCount; j++) {
            View chindView=getChildAt(i);
            chindView.layout(i,i1,i2,i3);
        }
    }
    private void init(){
        setOrientation(VERTICAL);
        View view=LayoutInflater.from(getContext()).inflate(R.layout.activity_addpanel,this,false);
        gridView= (GridView) view.findViewById(R.id.gridView);
        PanelItemAdapter adapter=new PanelItemAdapter(getContext(), ToolUtils.getLancherInfoList(getContext()),true);
        gridView.setAdapter(adapter);
        addView(view);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count=getChildCount();
        for (int i = 0; i < count; i++) {
            View childView=getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    public int getmCurrentX() {
        return mCurrentX;
    }

    public void setmCurrentX(int mCurrentX) {
        this.mCurrentX = mCurrentX;
    }

    public int getmCurrentY() {
        return mCurrentY;
    }

    public void setmCurrentY(int mCurrentY) {
        this.mCurrentY = mCurrentY;
    }


    public void changeCirlceColor(boolean b,int pos){
        isChangeColor=b;
        this.pos=pos;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }
    public void notifyData() {
        PanelItemAdapter adapter=new PanelItemAdapter(getContext(),ToolUtils.getLancherInfoList(getContext()),true);
        gridView.setAdapter(adapter);
    }


    private void drawCircle(Canvas canvas) {
        Paint circlePaint=new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(30);
        circlePaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
        canvas.drawCircle(getmCurrentX(),getmCurrentY(),arcRadiu,circlePaint);
        postInvalidateDelayed(1);
        for (int i = 0; i < PanelItemAdapter.EmptyListItemSize; i++) {
            ImageView circle= (ImageView) gridView.getChildAt(i).findViewById(R.id.circle);
            if(isChangeColor && pos==i){
                circle.setImageDrawable(getContext().getResources().getDrawable(R.drawable.panel_item_blue_shape));
            }else{
                circle.setImageDrawable(getContext().getResources().getDrawable(R.drawable.panel_item_grey_shape));

            }
        }

    }

}
