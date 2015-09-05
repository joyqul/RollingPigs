package hackathon.nctucs.rollingpigs.elements;

import android.widget.ImageView;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class Slot {

    private int id, x, y;
    private int content;
    private ImageView img;


    public Slot( int _id , int _x , int _y , int _content ){
        id = _id;
        x  = _x;
        y  = _y;
        content = _content;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getContent(){
        return content;
    }

}
