package cn.edu.nju.software.gof.requests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CachUtilities {

	private static InputStream getImageCach(String id, File cachDir) {
		File file = new File(cachDir, id);
		if (file.exists()) {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
			}
		}
		return null;
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		try {
			synchronized (in) {
				synchronized (out) {
					byte[] buffer = new byte[256];
					// start copy
					while (true) {
						int bytesRead = in.read(buffer);
						if (bytesRead == -1)
							break;
						out.write(buffer, 0, bytesRead);
					}
					// end copy
					out.flush();
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			out.close();
			in.close();
		}
	}

	private static byte[] saveImageCach(String id, InputStream stream,
			File cachDir) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			copy(stream, out);
			byte[] buffer = out.toByteArray();
			File file = new File(cachDir, id);

			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(buffer);
			outStream.close();
			return buffer;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	private static File getCachImage(final String id, File cachDir) {
		File[] list = cachDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.contains(id);
			}
		});
		if (list != null && list.length != 0) {
			return list[0];
		} else {
			return null;
		}
	}

	public static boolean updateAvatar(String sessionID, InputStream avatar,
			int contentLength, File cachDir) {
		byte[] buffer = saveImageCach(sessionID, avatar, cachDir);
		return InformationUtilities.uploadAvatar(sessionID,
				new ByteArrayInputStream(buffer), contentLength);
	}

	public static InputStream getFriendAvatar(String sessionID,
			String friendID, File cachDir) {
		Long counter = FriendUtilities.getFriendAvatarCounter(sessionID,
				friendID);
		if (counter == null) {
			return null;
		}
		File cache = getCachImage(friendID, cachDir);
		if (cache == null) {
			File file = new File(cachDir, friendID + "_" + counter);
			boolean success = FriendUtilities.getFriendAvatar(sessionID,
					friendID, file);
			if (success) {
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return null;
			}
		} else {
			String fileName = cache.getName();
			String currentCounter = fileName.split("_")[1];
			if (currentCounter.equals(counter.toString())) {
				try {
					return new FileInputStream(cache);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} else {
				cache.delete();
				File file = new File(cachDir, friendID + "_" + counter);
				boolean success = FriendUtilities.getFriendAvatar(sessionID,
						friendID, file);
				if (success) {
					try {
						return new FileInputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			}
		}
		// InputStream stream = getImageCach(friendID, cachDir);
		// if (stream != null) {
		// return stream;
		// } else {
		// File file = new File(cachDir, friendID);
		// boolean success = FriendUtilities.getFriendAvatar(sessionID,
		// friendID, file);
		// if (success) {
		// try {
		// return new FileInputStream(file);
		// } catch (FileNotFoundException e) {
		// return null;
		// }
		// } else {
		// return null;
		// }
		// }
	}

	public static InputStream getPersonalAvater(String sessionID, File cachDir) {
		InputStream stream = getImageCach(sessionID, cachDir);
		if (stream != null) {
			return stream;
		} else {
			File file = new File(cachDir, sessionID);
			boolean success = InformationUtilities.getPersonalAvatar(sessionID,
					file);
			if (success) {
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	public static InputStream getPlaceAvater(String sessionID, String placeID,
			File cachDir) {
		Long counter = PlaceUtilities.getPlaceImageCounter(sessionID, placeID);
		if (counter == null) {
			return null;
		}
		File cache = getCachImage(placeID, cachDir);
		if (cache == null) {
			File file = new File(cachDir, placeID + "_" + counter);
			boolean success = PlaceUtilities.getPlaceImage(sessionID, placeID,
					file);
			if (success) {
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return null;
			}
		} else {
			String fileName = cache.getName();
			String currentCounter = fileName.split("_")[1];
			if (currentCounter.equals(counter.toString())) {
				try {
					return new FileInputStream(cache);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} else {
				cache.delete();
				File file = new File(cachDir, placeID + "_" + counter);
				boolean success = PlaceUtilities.getPlaceImage(sessionID,
						placeID, file);
				if (success) {
					try {
						return new FileInputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			}
		}
		// InputStream stream = getImageCach(placeID, cachDir);
		// if (stream != null) {
		// return stream;
		// } else {
		// File file = new File(cachDir, placeID);
		// boolean success = PlaceUtilities.getPlaceImage(sessionID, placeID,
		// file);
		// if (success) {
		// try {
		// return new FileInputStream(file);
		// } catch (FileNotFoundException e) {
		// return null;
		// }
		// } else {
		// return null;
		// }
		// }
	}
}
