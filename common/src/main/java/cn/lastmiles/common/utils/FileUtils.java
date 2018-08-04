package cn.lastmiles.common.utils;

import java.io.IOException;

import jodd.io.FileNameUtil;
import jodd.io.FileUtil;

public interface FileUtils {

	/**
	 * 递归删除目录，文件，小心使用
	 * 
	 * @param path
	 */
	public static void delete(String path) {
		try {
			FileUtil.delete(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 文件后缀
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		return FileNameUtil.getExtension(filename);
	}

	/**
	 * a/b/c.txt --> c.txt a.txt --> a.txt a/b/c --> c a/b/c/ --> "" 文件名称
	 * 
	 * @param filename
	 * @return
	 */
	public static String getName(String filename) {
		return FileNameUtil.getName(filename);
	}
}
