package cn.edu.nju.software.gof.network;

import java.io.InputStream;

import org.apache.http.entity.mime.content.InputStreamBody;

class InputStreamKnownSizeBody extends InputStreamBody {
	private int length;

	public InputStreamKnownSizeBody(final InputStream in, final int length,
			final String mimeType, final String filename) {
		super(in, mimeType, filename);
		this.length = length;
	}

	@Override
	public long getContentLength() {
		return this.length;
	}
}
