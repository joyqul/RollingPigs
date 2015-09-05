package hackathon.nctucs.rollingpigs.elements;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class Circle {

    private int src;
    private int id, color, x, y;
    private double radius;
    private List< Integer > slots = new ArrayList();
    public ImageView img;

    public Circle( int _id, int _color, int _x , int _y , double _radius , int _src ){
        id = _id;
        color = _color;
        x = _x;
        y = _y;
        radius = _radius;
        src = _src;
    }
    public void addSlot( Integer _slot ){
        slots.add( _slot );
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getSrc(){
        return src;
    }
    public double getRadius(){
        return radius;
    }


}
