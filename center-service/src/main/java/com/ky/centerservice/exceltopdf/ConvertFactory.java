package com.ky.centerservice.exceltopdf;

import java.io.OutputStream;

public interface ConvertFactory {
	public void convert(String srcPath, OutputStream os);
}
