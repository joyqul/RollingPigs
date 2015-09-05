package hackathon.nctucs.rollingpigs.elements;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Map;

/**
 * Created by Shamrock on 2015/9/5.
 * Modified by Deepshine.
 */
public class Node{


	public boolean valid;
	private int id, onSId, color;
	private int radius;
	private int src;
	public ImageView img;


	public Node(int _id, int _color, int _onSId, int _radius, int _src){

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

	public int getColor(){
		return color;
	}

	public void animate(int circleX, int circleY, int circleR, int fromX, int fromY, int targetX, int targetY){
		final RectF rect = new RectF(circleX-circleR-radius, circleY-circleR-radius, circleX+circleR-radius, circleY+circleR-radius);
		Log.e(""+(circleX-circleR), ""+(circleX+circleR));
		Log.e(""+(circleY-circleR), ""+(circleY+circleR));
		Path path = new Path();
		int[] deg = degreeRange(circleX, circleY, fromX, fromY, targetX, targetY);

		Log.e("!!!!", circleX+" "+circleY+" "+fromX+" "+fromY+" "+targetX+" "+targetY);

		Log.e(deg[0]+" ", deg[1]+" ");

		path.addArc(rect, deg[0], (deg[1]-deg[0]+360)%360);
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img, View.X, View.Y, path);
		objectAnimator.setDuration(1000);

		ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(img, "rotate", 0.0f, 360.0f);
		rotateAnimator.setDuration(1000);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(objectAnimator).with(rotateAnimator);
		animatorSet.start();
		//objectAnimator.start();

		/*Animation animation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setRepeatCount(0);
		animation.setDuration(1000);
		img.startAnimation(animation);*/
	}

	public void setOnSId(int _sid){
		onSId = _sid;
	}

	int[] degreeRange(int centerX, int centerY, int startX, int startY, int targetX, int targetY){
		int v1X = startX-centerX, v1Y = startY-centerY;
		int v2X = targetX-centerX, v2Y = targetY-centerY;
		return new int[]{(int)(Math.atan2(v1Y, v1X)*180/Math.PI), (int)(Math.atan2(v2Y, v2X)*180/Math.PI)};
	}

}
