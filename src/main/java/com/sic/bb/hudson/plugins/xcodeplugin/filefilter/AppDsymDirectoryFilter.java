package com.sic.bb.hudson.plugins.xcodeplugin.filefilter;

import java.io.File;


public final class AppDsymDirectoryFilter extends DirectoryFilter {
	private static final long serialVersionUID = 1L;
	public static final String FILE_ENDING = ".app.dSYM";

	public boolean accept(File dir, String filename) {
		return super.accept(dir, filename) && filename.endsWith(FILE_ENDING);
	}
}
