package bulkContactsImport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etouch.segment.ReadSegmentData;

public class UploadTest {

	public static void main(String[] args) throws ServletException {
		// TODO Auto-generated method stub
		GoogleEngineContactUploadServlet uplaod=new GoogleEngineContactUploadServlet();
		HttpServletRequest req = null;
		HttpServletResponse resp = null;
		try {
			uplaod.doPost(req, resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
