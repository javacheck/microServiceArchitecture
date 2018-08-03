package cn.self.cloud.commonutils.compress;

import cn.self.cloud.commonutils.validate.ValidateUtils;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import java.io.*;
import java.util.Enumeration;

/**
 * 压缩/解压缩
 */
public abstract class CompressFileUtils {
    /**
     * 功能：压缩文件
     * @param srcfile File[] 需要压缩的文件列表
     * @param zipfile File 压缩后的文件
     */
    public static void ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            int length = srcfile.length;
            for (int i = 0; i < length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：zip解压缩文件
     * @param zipfile File 需要解压缩的文件
     * @param descDir String 解压后的目标目录
     */
    public static void unZipFiles(File zipfile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration<ZipEntry> entries = zf.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (entries.nextElement());
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：根据原始rar路径，解压到指定文件夹下.
     * @param srcRarPath       原始rar路径
     * @param dstDirectoryPath 解压到的文件夹
     */
    public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
        File dstDirectory = new File(dstDirectoryPath);
        // 目标目录不存在时，创建该文件夹
        if (!dstDirectory.exists()) {
            dstDirectory.mkdirs();
        }
        Archive archive;
        try {
            archive = new Archive(new File(srcRarPath));
            if (ValidateUtils.isNotEmpty(archive)) {
                FileHeader fh = archive.nextFileHeader();
                while (fh != null) {
                    // 文件夹
                    if (fh.isDirectory()) {
                        File fol = new File((dstDirectoryPath + File.separator + fh.getFileNameString()).replaceAll("\\\\", "/"));
                        fol.mkdirs();
                    } // 文件
                    else {
                        File out = new File((dstDirectoryPath + File.separator + fh.getFileNameString().trim()).replaceAll("\\\\", "/"));
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            archive.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = archive.nextFileHeader();
                }
                archive.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 单元测试
    public static void main(String[] args) {
        File s = new File("S:\\serverSpace\\nginx-1.13.1\\conf");
        File d = new File("S:\\test.rar");
        ZipFiles(s.listFiles(),d);
        unZipFiles(d,"S:\\D");
        unRarFile(d.getAbsolutePath(),"S:\\D");
    }
}
