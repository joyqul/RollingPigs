package hackathon.nctucs.rollingpigs.elements;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class Circle {

    private int src;
    private int id, color, x, y;
    private double radius;
    private List< Integer > slots = new ArrayList();
    private Map< Integer , Node > m_nodes;
    private Map< Integer , Slot > m_slots;
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
    public void setAttr( Map<Integer,Node> _nodes , Map<Integer,Slot> _slots ){
        m_nodes = _nodes;
        m_slots = _slots;

    }
    public void rotate(){


        List<Integer> r_slots = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();

        for ( int s_id : slots ){
            nodes.add( m_nodes.get( m_slots.get( s_id ).getContent() ) );
        }


        for ( int i = 0 ; i < slots.size() ; i++ ){


            int now_sid    = slots.get( i );
            int target_sid = slots.get( (i+1)%slots.size() );


            nodes.get( i ).animate(
                x,
                y,
                2*(int)radius,
                m_slots.get( now_sid ).getX(),
                m_slots.get( now_sid ).getY(),
                m_slots.get( target_sid ).getX(),
                m_slots.get( target_sid ).getY()
            );

            nodes.get( i ).setOnSId( target_sid )   ;
            m_slots.get( target_sid ).setContent( nodes.get( i ).getId() );
        }




    }


}
