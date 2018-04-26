package br.com.paulosalvatore.codelab_android_a9_mt_menu_25_04_18;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView ivImagem;

    private final String urlImagem = "https://www.amazonasnoticias.com.br/wp-content/uploads/2016/01/Samsung-Ocean.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImagem = findViewById(R.id.ivImagem);
    }

    public void workerThread(View view) {
        Toast.makeText(this, "onClick Worker Thread", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
                @Override
                public void run() {
                    ivImagem.post(new Runnable() {
                        @Override
                        public void run() {
                            ivImagem.setImageResource(android.R.color.transparent);
                        }
                    });

                    final Bitmap bitmap = carregarImagem(urlImagem);

                    ivImagem.post(new Runnable() {
                        @Override
                        public void run() {
                            ivImagem.setImageBitmap(bitmap);
                        }
                    });
                }
        }).start();
    }

    private Bitmap carregarImagem(String urlImagem) {
        try {
            URL url = new URL(urlImagem);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bitmap;
        }
        catch (Exception e) {
            Log.d("IMAGEM", e.toString());
        }

        return null;
    }

    public void asyncTask(View view) {
        new TarefaAssincrona().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnBusca:
                Toast.makeText(this, "Menu Busca clicado.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnInfo:
                Toast.makeText(this, "Menu Informações clicado.", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TarefaAssincrona extends AsyncTask<Void, Void, Bitmap> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "Carregando", "Carregando imagem...");

            ivImagem.setImageResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            dialog.dismiss();
            ivImagem.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return carregarImagem(urlImagem);
        }
    }
}
