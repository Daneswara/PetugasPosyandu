package com.masbie.petugasposyandu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailProfilAnak extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public boolean cek = true;
    public EditText textTTL, textNama, textBerat, textAyah, textIbu, textAlamat, textTelp, textAnakke;
    public RadioButton laki, perempuan;
    RadioGroup JK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_anak);
        setTitle("Detail Profil Anak");
        final String id = getIntent().getExtras().getString("id");
        String nama = getIntent().getExtras().getString("nama");
        String tanggalahir = getIntent().getExtras().getString("tanggalahir");
        String beratlahir = getIntent().getExtras().getString("beratlahir");
        String ayah = getIntent().getExtras().getString("ayah");
        String ibu = getIntent().getExtras().getString("ibu");
        String alamat = getIntent().getExtras().getString("alamat");
        String telp = getIntent().getExtras().getString("telp");
        String jeniskelamin = getIntent().getExtras().getString("jeniskelamin");
        String anakke = getIntent().getExtras().getString("anakke");

        textNama = (EditText) findViewById(R.id.textNama);
        textTTL = (EditText) findViewById(R.id.textTTL);
        textBerat = (EditText) findViewById(R.id.textBerat);
        textAyah = (EditText) findViewById(R.id.textAyah);
        textIbu = (EditText) findViewById(R.id.textIbu);
        textAlamat = (EditText) findViewById(R.id.textAlamatt);
        textTelp = (EditText) findViewById(R.id.textTelp);
        laki = (RadioButton) findViewById(R.id.laki);
        JK = (RadioGroup) findViewById(R.id.textJK);
        perempuan = (RadioButton) findViewById(R.id.perempuan);
        if(jeniskelamin.equals("Laki-laki")){
            laki.setChecked(true);
        } else {
            perempuan.setChecked(true);
        }
        textAnakke = (EditText) findViewById(R.id.anakke);

        textNama.setText(nama);
        textTTL.setText(tanggalahir);
        textBerat.setText(beratlahir);
        textAyah.setText(ayah);
        textIbu.setText(ibu);
        textAlamat.setText(alamat);
        textTelp.setText(telp);
        textAnakke.setText(anakke);

        Button simpan = (Button) findViewById(R.id.simpan);
        Button hapus = (Button) findViewById(R.id.delete);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = JK.getCheckedRadioButtonId();
                RadioButton input_jeniskelamin = (RadioButton) findViewById(selectedId);
                new AttemptSubmit(id, textNama.getText().toString(), textTTL.getText().toString(),
                        textBerat.getText().toString(), textAyah.getText().toString(), textIbu.getText().toString(),
                        textAlamat.getText().toString(), textTelp.getText().toString(), input_jeniskelamin.getText().toString(),
                        textAnakke.getText().toString()).execute();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(DetailProfilAnak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah anda yakin?")
                        .setContentText("Anda tidak dapat mengembalikan data yang telah dihapus")
                        .setCancelText("Batal")
                        .setConfirmText("Iya")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                new HapusAnak(id).execute();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        textTTL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (cek) {
                    Calendar now = Calendar.getInstance();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            DetailProfilAnak.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                    dpd.setMaxDate(now);
                    dpd.setOkText("Pilih");
                    dpd.setCancelText("Batal");
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                    cek = false;
                } else {
                    cek = true;
                }
            }
        });
        textTTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        DetailProfilAnak.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setMaxDate(now);
                dpd.setOkText("Pilih");
                dpd.setCancelText("Batal");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
        textTTL.setText(date);
    }

    private ProgressDialog pDialog;

    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String id, nama, berat, ttl, ayah, ibu, alamat, telp, jeniskelamin, anakke;

        public AttemptSubmit(String id, String nama, String ttl, String berat, String ayah, String ibu, String alamat, String telp, String jeniskelamin, String anakke) {
            this.telp = telp;
            this.nama = nama;
            this.ttl = ttl;
            this.berat = berat;
            this.ayah = ayah;
            this.ibu = ibu;
            this.alamat = alamat;
            this.id = id;
            this.jeniskelamin = jeniskelamin;
            this.anakke = anakke;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailProfilAnak.this);
            pDialog.setMessage("Proses Input...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("telp", telp));
            nameValuePairs.add(new BasicNameValuePair("nama", nama));
            nameValuePairs.add(new BasicNameValuePair("ttl", ttl));
            nameValuePairs.add(new BasicNameValuePair("berat", berat));
            nameValuePairs.add(new BasicNameValuePair("ayah", ayah));
            nameValuePairs.add(new BasicNameValuePair("ibu", ibu));
            nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
            nameValuePairs.add(new BasicNameValuePair("jeniskelamin", jeniskelamin));
            nameValuePairs.add(new BasicNameValuePair("anakke", anakke));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/edit_anak.php?id=" + id);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                String responseAsString = EntityUtils.toString(response.getEntity());
                return responseAsString;

            } catch (ClientProtocolException e) {
                return e.toString();
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            pDialog.dismiss();
            if (result != null) {
                Toast.makeText(DetailProfilAnak.this, result, Toast.LENGTH_LONG).show();
            }
        }

    }

    class HapusAnak extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String id;

        public HapusAnak(String id) {
            this.id = id;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailProfilAnak.this);
            pDialog.setMessage("Proses Hapus...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/delete_anak.php?id=" + id);

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                String responseAsString = EntityUtils.toString(response.getEntity());
                return responseAsString;

            } catch (ClientProtocolException e) {
                return e.toString();
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            pDialog.dismiss();
            if (result != null) {
                Toast.makeText(DetailProfilAnak.this, result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailProfilAnak.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}