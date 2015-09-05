package hackathon.nctucs.rollingpigs.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class Circle {

    private String src;
    private int id, color, x, y;
    private double radius;
    private List< Slot > slots = new ArrayList<Slot>();


    public Circle( int _id, int _color, int _x , int _y , double _radius , String _src ){
        id = _id;
        color = _color;
        x = _x;
        y = _y;
        radius = _radius;
        src = _src;
    }
    public void addSlot( Slot _slot ){
        slots.add( _slot );
    }



}
