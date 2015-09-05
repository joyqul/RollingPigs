package hackathon.nctucs.rollingpigs.elements;

import android.widget.ImageView;

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


    public int getSrc(){
        return src;
    }
    public double getRadius(){
        return radius;
    }

}
