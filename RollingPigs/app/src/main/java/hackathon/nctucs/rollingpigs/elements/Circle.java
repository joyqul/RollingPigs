package hackathon.nctucs.rollingpigs.elements;

import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.*;

/**
 * Created by Shamrock on 2015/9/5.
 * Modified by Deepshine.
 */

public class Circle {

    private int src;
    private int id, color, x, y;
    private double radius;
    public List< Integer > slots = new ArrayList();
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
        slots.add(_slot);

    }

    public void sortSlots(){
        Collections.sort(slots, new Cmp());

    }

	class Cmp implements Comparator<Integer>{

		@Override
		public int compare(Integer a, Integer b){
			Slot lhs = m_slots.get(a);
			Slot rhs = m_slots.get(b);
			int v1x = lhs.getX() - x, v1y = lhs.getY() - y;
			int v2x = rhs.getX() - x, v2y = rhs.getY() - y;
			double d1 = Math.atan2(v1y, v1x);
			double d2 = Math.atan2(v2y, v2x);
			return Double.compare(d1, d2);
		}

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
    public int getColor(){
        return color;
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
            /*Log.e( "fromX  fromY" , m_slots.get( now_sid ).getX() + " " +
                    m_slots.get( now_sid ).getY() );
            Log.e( "toX  toY" , m_slots.get( target_sid ).getX() + " " +
                    m_slots.get( target_sid ).getY() );*/
            nodes.get( i ).animate(
                x,
                y,
                (int)radius,
                m_slots.get( now_sid ).getX(),
                m_slots.get( now_sid ).getY(),
                m_slots.get( target_sid ).getX(),
                m_slots.get( target_sid ).getY()
            );

            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)nodes.get(i).img.getLayoutParams();
            lp.setMargins( (int)(m_slots.get( target_sid ).getX() - nodes.get( i ).getRadius() )
            ,(int)(m_slots.get( target_sid ).getY() - nodes.get( i ).getRadius() ),
            0 , 0
            );
            nodes.get( i ).img.setLayoutParams( lp );

           // Log.e("animate" , now_sid + " " + target_sid);
            nodes.get( i ).setOnSId( target_sid )   ;
            m_slots.get( target_sid ).setContent( nodes.get( i ).getId() );
        }
    }

}
