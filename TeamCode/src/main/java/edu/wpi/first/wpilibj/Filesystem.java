package edu.wpi.first.wpilibj;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Filesystem {
  public static void extractAssets(Context context, String assetPath, File outDir) {
    try {
      String[] children = context.getAssets().list(assetPath);
      if (children == null || children.length == 0) {
        // it's a file, extract it
        File outFile = new File(outDir, assetPath.substring(assetPath.lastIndexOf('/') + 1));
        try (InputStream in = context.getAssets().open(assetPath);
            FileOutputStream out = new FileOutputStream(outFile)) {
          byte[] buf = new byte[4096];
          int len;
          while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
        }
      } else {
        // it's a directory, recurse
        File subDir = new File(outDir, assetPath.substring(assetPath.lastIndexOf('/') + 1));
        subDir.mkdirs();
        for (String child : children) {
          extractAssets(context, assetPath + "/" + child, subDir);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static File getDeployDirectory(Context context) {
    try {
      File deployDir = new File(context.getFilesDir(), "deploy");
      File stampFile = new File(context.getFilesDir(), "deploy.stamp");

      long apkModified = new File(context.getPackageCodePath()).lastModified();

      boolean needsExtract =
          !deployDir.exists() || !stampFile.exists() || stampFile.lastModified() < apkModified;

      if (needsExtract) {
        // clean old files
        deleteRecursive(deployDir);
        deployDir.mkdirs();

        String[] files = context.getAssets().list("deploy");
        if (files != null) {
          for (String file : files) {
            extractAssets(context, "deploy/" + file, deployDir);
          }
        }

        stampFile.createNewFile();
        stampFile.setLastModified(apkModified);
      }
      return deployDir;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void deleteRecursive(File f) {
    if (f.isDirectory()) {
      for (File child : f.listFiles()) deleteRecursive(child);
    }
    f.delete();
  }
}
