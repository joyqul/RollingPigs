package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class MenuPage extends Activity {

    final int[] ids = new int[]{ 0 , R.id.stage1 , R.id.stage2 , R.id.stage3 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.menupage );


        for ( int i = 1 ; i <= 1  ; i++ ){
            ImageView img = (ImageView)findViewById( ids[i] );
            img.setTag( i );
            img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuPage.this , MainActivity.class);
                        intent.putExtra( "stage" , (int)v.getTag() );
                        startActivity( intent );
                    }
                }

            );

        }


    }
}
