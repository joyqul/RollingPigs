package hackathon.nctucs.rollingpigs.elements;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Map;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class Node {

    private int id, onSId, color;
    private int radius;
    private int src;
    public ImageView img;


    public Node( int _id , int _color , int _onSId , int _radius , int _src ){

        id = _id;
        color = _color;
        onSId = _onSId;
        src = _src;
        radius = _radius;

    }


    public int getOnSId(){
        return onSId;
    }

    public int getId(){
        return id;
    }
    public int getSrc(){
        return src;
    }
    public double getRadius(){
        return radius;
    }
    public void animate( int circleX , int circleY , int circleR , int fromX , int fromY , int targetX , int targetY ){

        radius *= 2;
        final RectF rect = new RectF( circleX-radius , circleY-radius , circleX+radius , circleY+radius );

        Path path = new Path();
        path.addArc( rect , 0  , 360 );
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img, View.X, View.Y, path);
        objectAnimator.setDuration( 1000 );
        objectAnimator.start();

    }
    public void setOnSId( int _sid ){
        onSId = _sid;
    }

	int[] degreeRange(int centerX, int centerY, int startX, int startY, int targetX, int targetY){
		int v1X = startX - centerX, v1Y = startY - centerY;
		int v2X = targetX - centerX, v2Y = targetY - centerY;
		return new int[]{(int)(Math.atan2(v1Y, v1X)*180/Math.PI), (int)(Math.atan2(v2Y, v2X)*180/Math.PI)};
	}

}
