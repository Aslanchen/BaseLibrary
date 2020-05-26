package com.aslan.baselibrary.utils;

import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 压缩文件
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/5/26
 */
public class ZipUtils {
    private static Logger mLogger = XLog.tag("ZipUtils").build();

    private ZipUtils() {
    }

    /**
     * 解压zip到指定的路径
     *
     * @param zipFileString ZIP的名称
     * @param outPathString 要解压缩路径
     */
    public static void UnZipFolder(String zipFileString, String outPathString) {
        mLogger.d("UnZipFolder zipFileString=%s outPathString=%s", zipFileString,
                outPathString);

        ZipInputStream inZip = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = null;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File folder = new File(outPathString + File.separator + szName);
                    folder.mkdirs();
                } else {
                    File file = new File(outPathString + File.separator + szName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }

                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = inZip.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                            out.flush();
                        }
                    } catch (Exception ex) {
                        mLogger.e("UnZipFolder read", ex);
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception ex) {
                                mLogger.e("UnZipFolder out close", ex);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            mLogger.e("UnZipFolder", ex);
        } finally {
            if (inZip != null) {
                try {
                    inZip.close();
                } catch (IOException ex) {
                    mLogger.e("UnZipFolder inZip close", ex);
                }
            }
        }
    }

    private static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     */
    @Nullable
    public static File ZipFolder(String srcFileString) {
        mLogger.d("ZipFolder srcFileString=%s", srcFileString);

        File srcFile = new File(srcFileString);
        if (!srcFile.exists()) {
            mLogger.e("srcFileString not exists");
            return null;
        }

        File outFile = null;
        if (srcFile.isDirectory()) {
            outFile = new File(srcFile.getParent(), srcFile.getName() + ".zip");
        } else {
            outFile = new File(srcFile.getParent(), getFileNameNoEx(srcFile.getName()) + ".zip");
        }
        mLogger.d("ZipFolder outFile=%s", outFile);

        if (outFile.exists()) {
            outFile.delete();
        }

        ZipOutputStream outZip = null;
        try {
            outZip = new ZipOutputStream(new FileOutputStream(outFile));
            ZipFiles(srcFile, outZip);
            outZip.finish();
        } catch (Exception ex) {
            mLogger.e("ZipFolder", ex);
        } finally {
            if (outZip != null) {
                try {
                    outZip.close();
                } catch (IOException ex) {
                    mLogger.e("ZipFolder outZip close", ex);
                }
            }
        }
        return outFile;
    }

    /**
     * 压缩文件
     *
     * @param srcFile        源文件
     * @param zipOutputSteam zip输出流
     */
    private static void ZipFiles(@NonNull File srcFile, @NonNull ZipOutputStream zipOutputSteam) {
        if (!srcFile.exists()) {
            mLogger.e("ZipFiles srcFile not exists");
            return;
        }

        if (srcFile.isFile()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(srcFile);
                ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                zipOutputSteam.putNextEntry(zipEntry);
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    zipOutputSteam.write(buffer, 0, len);
                }
            } catch (Exception ex) {
                mLogger.e("ZipFiles", ex);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ex) {
                        mLogger.e("ZipFiles inputStream close", ex);
                    }
                }

                try {
                    zipOutputSteam.closeEntry();
                } catch (IOException ex) {
                    mLogger.e("ZipFiles zipOutputSteam closeEntry", ex);
                }
            }
        } else {
            String[] fileList = srcFile.list();
            if (fileList == null || fileList.length <= 0) {
                try {
                    ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                    zipOutputSteam.putNextEntry(zipEntry);
                } catch (Exception ex) {
                    mLogger.e("ZipFiles putNextEntry", ex);
                } finally {
                    try {
                        zipOutputSteam.closeEntry();
                    } catch (IOException ex) {
                        mLogger.e("ZipFiles zipOutputSteam closeEntry", ex);
                    }
                }
                return;
            }

            //子文件和递归
            for (String s : fileList) {
                ZipFiles(new File(srcFile, s), zipOutputSteam);
            }
        }
    }
}
