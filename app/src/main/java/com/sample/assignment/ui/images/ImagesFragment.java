package com.sample.assignment.ui.images;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sample.assignment.R;
import com.sample.assignment.utils.ImageAsyncTask;
import com.sample.assignment.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ImagesFragment extends Fragment implements ImageAsyncTask.ImageCallback {

    private static final String IMAGE_DIRECTORY_NAME = "App" ;
    private AppCompatButton buttonGallery;
    private AppCompatButton buttonCamera;
    private View viewImage;
    private View viewImageTwo;
    private ImageView imageDisplay;
    private ImageView imageDisplayTwo;
    private TextView tvAddPhotoOne;
    private TextView tvAddPhotoTwo;
    private AppCompatTextView appCompatTextViewOne;
    private AppCompatTextView appCompatTextViewTwo;
    private static boolean  gallery = true;
    private static int imageNo = 1;
    private Uri fileUri;
    private static final int  REQUEST_IMAGE_FROM_GALLERY = 100;
    private static final int  REQUEST_IMAGE_CAPTURE = 101;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ){
        super.onViewCreated(view, savedInstanceState);

        buttonGallery = (AppCompatButton)view.findViewById(R.id.btnGallery);
        buttonCamera = (AppCompatButton)view.findViewById(R.id.btnCamera);

        viewImage = (View)view.findViewById(R.id.viewOne);
        imageDisplay = viewImage.findViewById(R.id.img_display);
        tvAddPhotoOne = viewImage.findViewById(R.id.tv_add_photo);

        viewImageTwo = (View)view.findViewById(R.id.viewTWO);
        imageDisplayTwo = viewImageTwo.findViewById(R.id.img_display);
        tvAddPhotoTwo = viewImageTwo.findViewById(R.id.tv_add_photo);

        appCompatTextViewOne = (AppCompatTextView)view.findViewById(R.id.tvImageInfoOne);
        appCompatTextViewTwo = (AppCompatTextView)view.findViewById(R.id.tvImageInfoTwo);

        buttonGallery.setOnClickListener(v -> {
            gallery = true;
            checkPermission();
        });

        buttonCamera.setOnClickListener(v -> {
            gallery = false;
            checkPermission();
        });

        viewImage.setOnClickListener(v -> {
            imageNo = 1;
            if (gallery){
                selectGallery();
            }else {
                selectCamera();
            }
        });

        viewImageTwo.setOnClickListener(v -> {
            imageNo = 2;
            if (gallery){
                selectGallery();
            }else {
                selectCamera();
            }
        });

    }

    @Override
    public void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent imageReturnedIntent
    ){
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK){
            String filePath = Util.getRealPathFromUri(getContext(), imageReturnedIntent.getDataString());
            // Log.e("ImagesFragment",filePath);
            setImage(filePath);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.e("REQUEST_IMAGE_CAPTURE", fileUri.getPath());
            setImage(fileUri.getPath());

        }
    }

    @Override
    public void setImageInfo(
            int image,@NotNull
            String imagePath
    ){
        File file = new File(imagePath);
        // Log.e("Filename", file.getName());
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        // Log.e("File size", String.valueOf(file_size));
        if (image == 1){
            appCompatTextViewOne.setText("File : "+ file.getName() +", \n" +" Size : " +file_size + " KB");
        }else {
            appCompatTextViewTwo.setText("File : "+ file.getName() +", \n" +" Size : " +file_size + " KB");
        }

    }

    @Override
    public void setImage(
            int image,
            @NotNull Bitmap bitmap
    ){
        if (image == 1){
            imageDisplay.setImageBitmap(bitmap);
            tvAddPhotoOne.setVisibility(View.GONE);
        }else {
            imageDisplayTwo.setImageBitmap(bitmap);
            tvAddPhotoTwo.setVisibility(View.GONE);
        }
    }

    private void dialogSelectType(){

        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialog.show();

        TextView tvSelectOne = dialog.findViewById(R.id.tvSelectOne);
        TextView tvSelectTwo = dialog.findViewById(R.id.tvSelectTwo);

        tvSelectOne.setOnClickListener(v -> {
            dialog.cancel();
            viewImage.setVisibility(View.VISIBLE);
            viewImageTwo.setVisibility(View.GONE);
            imageDisplay.setImageBitmap(null);
            tvAddPhotoOne.setVisibility(View.VISIBLE);
            appCompatTextViewOne.setText("");
            appCompatTextViewTwo.setText("");
        });

        tvSelectTwo.setOnClickListener(v -> {
            dialog.cancel();
            viewImage.setVisibility(View.VISIBLE);
            viewImageTwo.setVisibility(View.VISIBLE);
            imageDisplay.setImageBitmap(null);
            imageDisplayTwo.setImageBitmap(null);
            tvAddPhotoTwo.setVisibility(View.VISIBLE);
            tvAddPhotoOne.setVisibility(View.VISIBLE);
            appCompatTextViewOne.setText("");
            appCompatTextViewTwo.setText("");
        });
    }

    private void selectGallery(){
        Intent pickPhoto = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(pickPhoto , REQUEST_IMAGE_FROM_GALLERY);
    }

    private void selectCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Util.getOutputMediaFileUri(getContext(), IMAGE_DIRECTORY_NAME);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void  setImage(String filePath){
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask(getContext(),this,imageNo);
        imageAsyncTask.execute(filePath);
    }

    private void checkPermission(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                dialogSelectType();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}