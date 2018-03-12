package com.luo.convert.base.filter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.luo.convert.base.constant.Constant;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by luoxmc on 18/3/12.
 */
public class UploadFilter extends OncePerRequestFilter implements Filter {
	private static Logger log = Logger.getLogger(UploadFilter.class);
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (ServletFileUpload.isMultipartContent(request)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileItemList = null;
            try {
                fileItemList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
                log.error("error -" + e.getMessage());
            }

            if (fileItemList != null) {
                log.debug("web 文件上传开始:");
                for (FileItem fi : fileItemList) {
                    if (fi.isFormField()) {
                        request.setAttribute(fi.getFieldName(), fi.getString());
                    } else {
                        if (StringUtils.isEmpty(fi.getName()) || (fi.getName().lastIndexOf(".") <= 0 || fi.getName().lastIndexOf(".") == (fi.getName().length() - 1))) {
                            request.setAttribute("uploadStatus", "0");
                            request.setAttribute("uploadMessage", "文件名称为空或格式不正确！");
                            break;
                        }
                        String sourceFileName = fi.getName();
                        String uploadPath = Constant.FILE_UPLOAD_PATH;
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        String today = sdf.format(date);
                        String filePath = uploadPath + File.separator + today + File.separator + String.valueOf(new Random().nextInt(900000)+100000) + File.separator;
                        File path = new File(filePath);
                        if(!path.exists()){
                        	path.mkdirs();
                        }
                        File file = new File(filePath + sourceFileName);
                        try {
                            fi.write(file);
                            fi.delete();
                            request.setAttribute("path", filePath);
                            request.setAttribute("name", sourceFileName);
                            request.setAttribute("uploadStatus", "1");
                            log.info("文件保存成功，路径为："+filePath + sourceFileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute("uploadStatus", "0");
                            request.setAttribute("uploadMessage", "上传文件失败！");
                            log.error("上传文件失败 -" +sourceFileName);
                            log.error("异常信息 -" + e.getMessage());
                            break;
                        }
                    }
                }
                log.debug("web 文件上传结束");
            }
        }

        filterChain.doFilter(request, response);
    }
}
