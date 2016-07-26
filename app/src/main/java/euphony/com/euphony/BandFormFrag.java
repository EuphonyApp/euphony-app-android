package euphony.com.euphony;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.rey.material.widget.Spinner;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pR0 on 04-07-2016.
 */
public class BandFormFrag extends Fragment {

    String gnr;
    ImageView camera, gallery, next;
    EditText location, name, contact, founded;
    Spinner genre;
    TextView subGenre;
    PrefManager prefManager;
    boolean result;
    String clicked;

    public BandRegisterActivity bandRegisterActivity;

    List<String> subGenreList = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    String all = "Select,Alternative Rock,Ambient,Blues,Classical,Country,Deep House," +
            "Drum & Bass,Electronic,Folk,Hip-Hop & Rap,Indie,Jazz,Metal,Pop,Reggae,Rock,Techno,Trap,Trip Hop,Other";

    List<String> spinnerList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        bandRegisterActivity = (BandRegisterActivity) getActivity();
        return inflater.inflate(R.layout.band_form_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ImageView) getActivity().findViewById(R.id.circle1)).setImageResource(R.drawable.circle_blue);
        ((ImageView) getActivity().findViewById(R.id.circle2)).setImageResource(R.drawable.circle_white);
        ((ImageView) getActivity().findViewById(R.id.circle3)).setImageResource(R.drawable.circle_white);

        camera = (ImageView) getView().findViewById(R.id.camera);
        gallery = (ImageView) getView().findViewById(R.id.gallery);

        location = (EditText) getView().findViewById(R.id.location);
        name = (EditText) getView().findViewById(R.id.name);
        contact = (EditText) getView().findViewById(R.id.contact);
        founded = (EditText) getView().findViewById(R.id.founded_on);

        genre = (Spinner) getView().findViewById(R.id.genre);
        subGenre = (TextView) getView().findViewById(R.id.sub_genre);

        spinnerList = Arrays.asList(all.split(","));

        prefManager = new PrefManager(Application.ctx);

        if (bandRegisterActivity.band != null) {
            subGenreList = bandRegisterActivity.band.getSubgenre();

            name.setText(bandRegisterActivity.band.getName());
            location.setText(bandRegisterActivity.band.getLocation());
            genre.setSelection(spinnerList.indexOf(bandRegisterActivity.band.getGenre()));
            gnr = bandRegisterActivity.band.getGenre();
            contact.setText(bandRegisterActivity.band.getContact());
            founded.setText(bandRegisterActivity.band.getFoundedOn());

            if(!bandRegisterActivity.band.getPic().isEmpty()) {
                Picasso.with(getActivity()).load(bandRegisterActivity.band.getPic()).error(R.drawable.ak).into(camera);
            } else {
                camera.setImageResource(R.drawable.ak);
            }
            camera.setClickable(false);
            gallery.setVisibility(View.GONE);
            String temp = "";

            for (int i = 0; i < subGenreList.size(); ++i)
                temp += subGenreList.get(i) + " ";

            subGenre.setText(temp);
        } else {

                bandRegisterActivity.band = new Band();

            bandRegisterActivity.band.setMembers(new ArrayList<String>());
            bandRegisterActivity.band.getMembers().add(prefManager.getId());
            bandRegisterActivity.band.setPic("");
            bandRegisterActivity.band.setPositions(new ArrayList<String>());
            bandRegisterActivity.band.getPositions().add("admin");
            bandRegisterActivity.band.setManager(prefManager.getId());
        }

        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_lay, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SubGenreActivity.class);
                if (subGenreList.size() != 0)
                    i.putStringArrayListExtra("list", (ArrayList<String>) subGenreList);
                startActivityForResult(i, 1);
            }
        });

        genre.setAdapter(dataAdapter);
        genre.setOnItemSelectedListener(new com.rey.material.widget.Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(com.rey.material.widget.Spinner parent, View view, int position, long id) {
                gnr = parent.getSelectedItem().toString();
                if (gnr.equals("Select"))
                    gnr = "";
            }
        });

        next = (ImageView) getView().findViewById(R.id.next_page);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString(), locText = location.getText().toString(), genreText = gnr,
                        subGenreText = subGenre.getText().toString(), contactText = contact.getText().toString(), foundedText = founded.getText().toString();
                if (nameText.isEmpty() || locText.isEmpty() || genreText.isEmpty() || subGenreText.isEmpty() || contactText.isEmpty() || foundedText.isEmpty()) {
                    SuperActivityToast.create(getActivity(), "Please fill the form completely!", SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                } else {
                    if (!contactText.matches("^[789]\\d{9}$"))
                        SuperActivityToast.create(getActivity(), "Please enter valid mobile number!", SuperToast.Duration.SHORT,
                                Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();

                    else {
                        if (bandRegisterActivity.band == null) {
                            bandRegisterActivity.band = new Band();
                        }

                        bandRegisterActivity.band.setName(nameText);
                        bandRegisterActivity.band.setContact(contactText);
                        bandRegisterActivity.band.setLocation(locText);
                        bandRegisterActivity.band.setGenre(genreText);
                        bandRegisterActivity.band.setSubgenre(subGenreList);
                        bandRegisterActivity.band.setFoundedOn(foundedText);

                        if(bandRegisterActivity.band.getPic().isEmpty())
                            SuperActivityToast.create(getActivity(), "Please choose a profile pic!", SuperToast.Duration.SHORT,
                                    Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                        else {
                            android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.band_form_container, new AddArtistFrag()).commit();
                        }
                    }
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = "camera";
                result = Utility.checkPermission(getActivity());
                if (result) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 2);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = "gallery";
                result = Utility.checkPermission(getActivity());
                if (result) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Band Image"), 3);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                subGenreList = data.getStringArrayListExtra("data");
                String temp = "";

                for (int i = 0; i < subGenreList.size(); ++i)
                    temp += subGenreList.get(i) + " ";

                subGenre.setText(temp);
            } else if(requestCode == 2)
                onCaptureImageResult(data);
            else if(requestCode == 3)
                onSelectFromGalleryResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (clicked.equals("camera")) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, 2);
                    } else if (clicked.equals("gallery")) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(i, "Select Band Image"), 3);
                    }
                } else {
                    SuperActivityToast.create(getActivity(), "Permission denied to set pic!!", SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                byte[] byte_arr = stream.toByteArray();
                String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                bandRegisterActivity.band.setPic(image_str);
                camera.setImageBitmap(bm);
                camera.setClickable(false);
                gallery.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        byte[] byte_arr = bytes.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        bandRegisterActivity.band.setPic(image_str);
        camera.setImageBitmap(thumbnail);

        camera.setClickable(false);
        gallery.setVisibility(View.GONE);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            //destination.u
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}