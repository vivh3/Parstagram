package me.vivh.parstagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.vivh.parstagram.model.Post;

import static android.app.Activity.RESULT_OK;

public class AddPostFragment extends Fragment {

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    Button button;
    ImageView imageView;
    EditText description;
    Button postBtn;
    String imagePath;
    String currentPath;
    ParseFile parseFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_post,
                container, false);

        button = (Button) rootView.findViewById(R.id.btnClick);
        imageView = (ImageView) rootView.findViewById(R.id.ivPreview);
        description = (EditText) rootView.findViewById(R.id.etDescription);
        postBtn = (Button) rootView.findViewById(R.id.btnPost);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Activity activity = AddPostFragment.this.getActivity();

                File mediaStorage = null;
                try {
                    mediaStorage = getImageFile(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Create the storage directory if it does not exist
                if (!mediaStorage.exists() && !mediaStorage.mkdirs()){
                    Log.d(APP_TAG, "failed to create directory");
                }

                String path = mediaStorage.getAbsolutePath();
                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                Uri fileProvider = FileProvider.getUriForFile(activity, "com.codepath.fileProvider", mediaStorage);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // Create a File reference to access to future access
                photoFile = new File(path);
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = description.getText().toString();
                final ParseUser currUser = ParseUser.getCurrentUser();
                final File file = photoFile;

                parseFile = new ParseFile(file);
                postPhoto(desc, parseFile, currUser);
            }
        });
        return rootView;
    }

    private static File getImageFile(Context context) throws IOException {
        ///storage/self/primary/DCIM/Camera/
        String currentTime = getCurrentTime();

        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/" + currentTime + ".jpg");
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                currentPath = photoFile.getPath();
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                imageView.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postPhoto(final String description, final ParseFile imageFile, final ParseUser user){
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    final Post newPost = new Post();
                    newPost.setDescription(description);
                    newPost.setImage(imageFile);
                    newPost.setUser(user);
                    newPost.saveInBackground();

                    Toast.makeText(getContext(),"Post created!",Toast.LENGTH_LONG).show();
                    Log.d("AddPostFragment", description);
                } else {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Failed to post",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
}
