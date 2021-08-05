package obil.home.utils;

import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public final class FileUtils {
    private static final String TAG = "FileUtils";
    private static final String EMULATED_DIR = "emulated";
    private static final String STORAGE_DIR = "/storage";

    private FileUtils() {
    }

    public static String getFilePathByFirstLetters(String dir, String firstLetters) {
        if (firstLetters == null) {
            return null;
        }
        String externalStorageRootDir = getExternalStorageRootDir();
        if (externalStorageRootDir == null) {
            Log.e(TAG, "Не найден внешний накопитель");
            return null;
        }
        File fullDirPath = new File(externalStorageRootDir + "/" + dir);
        if (fullDirPath.exists()) {
            String result = Arrays.stream(Objects.requireNonNull(fullDirPath.listFiles()))
                    .peek(f -> System.out.println("before first letters " + f.getPath()))
                    .filter(f -> f.getName().toLowerCase().startsWith(firstLetters.toLowerCase()))
                    .map(File::getPath)
                    .findFirst()
                    .orElse(null);
            Log.i(TAG, "getFilePathByFirstLetters() result " + result);
            return result;
        }
        Log.i(TAG, "!(fullDirPath.exists()) " + fullDirPath.getPath());
        return null;
    }

    private static String getExternalStorageRootDir() {
        File storageDir = new File(STORAGE_DIR);
        if (storageDir.exists() && storageDir.isDirectory()) {
            String result = Arrays.stream(Objects.requireNonNull(storageDir.listFiles()))
                    .peek(f -> System.out.println("before emul " + f.getPath()))
                    .filter(f -> !f.getName().equalsIgnoreCase(EMULATED_DIR))
                    .map(File::getPath)
                    .peek(p -> System.out.println("after emul " + p))
                    .findFirst()
                    .orElse(null);
            Log.i(TAG, "getExternalStorageRootDir() result " + result);
            return result;
        }
        Log.i(TAG, "!(storageDir.exists() && storageDir.isDirectory()) " + storageDir.getPath());
        return null;
    }
}
