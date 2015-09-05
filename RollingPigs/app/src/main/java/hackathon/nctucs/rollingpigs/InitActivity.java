package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class InitActivity extends Activity{

    ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.init );

        btn = (ImageView) findViewById( R.id.startGame );
        btn.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( InitActivity.this , MenuPage.class );
                    startActivity( intent );
                    InitActivity.this.finish();
                }
            }

        );

    }
}
