# Flipper

Flipper is an android library project to help developers to use file-like interface using the
storage access framework. Flipper is designed to handle the new behavior required by Android Q.

## Dependencies
* Android-X package
* Min version to use Flipper is Android KitKat (API 19)

## Set up Instructions
Set up the project dependencies. To use this library in your project:

(1) Use the GitHub source and include that as a module dependency by following these steps:
 * Clone this library into a project named SmartRecyclerView, parallel to your own application project:
```shell
git clone https://github.com/baldapps/Flipper.git
```
 * In the root of your application's project edit the file "settings.gradle" and add the following lines:
```shell
include ':flipper'
project(':flipper').projectDir = new File('../Flipper/flipper')
```
 * In your application's main module (usually called "app"), edit your build.gradle to add a new dependency:
```shell
 dependencies {
    ...
    compile project(':flipper')
 }
```
Now your project is ready to use this library

## Main Features
Flipper provides an abstaction layer to manage files using Uri access on Android platform.

 Example and how to use the library:
 ```java
        public class MainActivity extends Activity {

           private StorageManagerCompat manager;

           @Override
           protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_main);
               manager = new StorageManagerCompat(this);
               Root root = manager.getRoot(StorageManagerCompat.DEF_MAIN_ROOT);
               if (root == null || !root.isAccessGranted(this)) {
                    Intent i = manager.requireExternalAccess(this);
                    startActivityForResult(i, 100);
               }
           }
        
           private void myMethod() {
                Root root = manager.getRoot(StorageManagerCompat.DEF_MAIN_ROOT);
                if (root == null)
                    return;
                DocumentFile f = root.toRootDirectory(this);
                if (f == null)
                    return;
                DocumentFile subFolder = DocumentFileCompat.peekSubFolder(f, "mysub");
                if (subFolder == null)
                    return; //directory doesn't exist yet
                DocumentFile myFile = DocumentFileCompat.peekFile(subFolder, "myfile", "image/png");
                if (myFile == null)
                    return; //file doesn't exist yet
                try {
                    InputStream is = getContentResolver().openInputStream(myFile.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
           }
        
           @Override
           protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == 100 && resultCode == RESULT_OK) {
                    Root root = manager.addRoot(this, StorageManagerCompat.DEF_MAIN_ROOT, data);
                    if (root == null)
                        return;
                    DocumentFile f = root.toRootDirectory(this);
                    if (f == null)
                        return;
                    try {
                        DocumentFile subFolder = DocumentFileCompat.getSubFolder(f, "mysub");
                        DocumentFile myFile = DocumentFileCompat.getFile(subFolder, "myfile", "image/png");
                        try {
                            OutputStream os = getContentResolver().openOutputStream(myFile.getUri());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } catch (OperationFailedException e) {
                        e.printStackTrace();
                    }
                }
           }
 ```

## References and how to report bugs
* If you find any issues with this library, please open a bug here on GitHub

## License
See LICENSE

## Change List

1.0.0
 * First version
