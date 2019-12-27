package com.greit.weys.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.greit.weys.ResValue;
import com.greit.weys.common.ErrCode;
import com.greit.weys.config.TokenValues;
import com.greit.weys.rsv.RsvService;
import com.greit.weys.user.UserService;

@Controller
public class ImageController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RsvService rsvService;
	@Autowired
	private UserService userService;

	@Value("${UPLOAD.PATH}")
	private String UPLOAD_ROOT_PATH;

	/**
	 * 이미지 보기
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @return 사용자 로그인 후 Token값 반환
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/imgView/{dir}/{path}")
	@ResponseBody
	public void streamImageView(HttpServletRequest request, HttpServletResponse response, @PathVariable String dir,
			@PathVariable String path) {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + request.getRequestURL());
		response.setContentType("text/plain;charset=UTF-8");

		try {
			if (dir == null || dir.equals("") || path == null || path.equals("")) {
				return;
			}

			StringTokenizer st = new StringTokenizer(request.getRequestURI().toString(), ".");
			String body = st.nextToken();
			String format = st.nextToken();

			String filePath = null;
			filePath = UPLOAD_ROOT_PATH + dir + File.separator + path + "." + format;
			logger.info("filePath ::: " + filePath);

			File f = new File(filePath);
			if (f.exists()) {
				InputStream is = new FileInputStream(f);
				IOUtils.copy(is, response.getOutputStream());
			} else {
				logger.info("errir ::: 이미지가 존재하지 않습니다. ::: " + dir + File.separator + path + "." + format);
			}
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
		}
		logger.info("===================================== END ===================================");
	}
	
	/**
	 * 이미지 업로드
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @return 사용자 로그인 후 Token값 반환
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/rsvDone")
	@ResponseBody
	public ResValue barogo(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + request.getRequestURL());

		ResValue result = new ResValue();

		// 필수 ::: 토큰값 체크
		TokenValues value = userService.checkCopToken(request, response, result);
		if (value == null) {
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		String rsvQr = request.getParameter("rsvQr");
		if(rsvQr.length() == 8){
			rsvQr = rsvQr.substring(0, 4) + " " + rsvQr.substring(4);
		}
		
		int resCnt = rsvService.checkRsvDone(rsvQr);
		if(resCnt == 0){
			result.setResCode(333);
			result.setResMsg("잘못된 예약입니다.");
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		
		String filePath = "";
		String dirName = "sign";

		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile file = multipartRequest.getFile("sign");

				String uploadFileName = "";
				if (!"".equals(file.getOriginalFilename())) {
					String original_filenm = file.getOriginalFilename();
					// 저장 파일명 지정
					String format = FilenameUtils.getExtension(original_filenm);
					uploadFileName = Calendar.getInstance().getTimeInMillis() + "." + format;

					String folder = UPLOAD_ROOT_PATH + dirName;
					File f_folder = new File(folder);
					if (!f_folder.exists()) {
						f_folder.mkdirs();
					}

					filePath = UPLOAD_ROOT_PATH + dirName + "/" + uploadFileName;
					file.transferTo(new File(filePath));
					
					resCnt = rsvService.updateRsvDone(rsvQr, value, dirName + "/" + uploadFileName);
				}

				Map<String, Object> resultMap = new HashMap<String, Object>();
				result.setResData(resultMap);
			} catch (Exception e) {
				logger.error("error ::: " + e.getMessage());
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			}
		} else {
			result.setResCode(444);
			result.setResMsg("파일 형식이 잘못되었습니다.");
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

}
