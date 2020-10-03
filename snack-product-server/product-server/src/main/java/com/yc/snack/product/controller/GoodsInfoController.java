package com.yc.snack.product.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yc.snack.product.VO.ResultVO;
import com.yc.snack.product.bean.GoodsInfo;
import com.yc.snack.product.enums.ResultEnum;
import com.yc.snack.product.service.IGoodsInfoService;
import com.yc.snack.product.util.StringUtil;

@RestController
@RequestMapping("/goods")
public class GoodsInfoController {
	@Autowired
	private IGoodsInfoService goodsInfoService;
	
	@PostMapping("/findByFirst")
	public ResultVO findByFirst(@RequestParam Map<String, Object> map) {
		return new ResultVO(ResultEnum.SUCCESS,goodsInfoService.findByFirst(map));
	}
	
	@PostMapping("/finds")
	public ResultVO finds(@RequestParam Map<String, Object> map) {
		return new ResultVO(ResultEnum.SUCCESS,goodsInfoService.finds(map));

	}
	@PostMapping("/findByPage")
	public Map<String,Object> findByPage(@RequestParam Map<String, Object> map){
		return goodsInfoService.findByPage(map);

	}
	
	@PostMapping("/upload")
	public Map<String,Object> uploadPic(HttpServletRequest request,@RequestParam("upload") MultipartFile pic){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(pic.isEmpty()) {
			return map;
		}
		try {
			String savePath = "pics";
			
			//取tomcat的绝对路径
			String path = request.getServletContext().getRealPath("");
			String temp = request.getServletContext().getInitParameter("uploadPath");
			if(!StringUtil.checkNull(temp)) {
				savePath = temp;
			}
			
			savePath += "/" + new Date().getTime() + "_" + pic.getOriginalFilename();
			File dest = new File(new File(path).getParentFile(),savePath);
			
			//将图片从本地保存到服务器
			pic.transferTo(dest);
			
			map.put("fileName", pic.getOriginalFilename());
			map.put("uploaded", 1);
			map.put("url", "../../../" + savePath);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@PostMapping("/add")
	public ResultVO addGoods(@RequestParam("picinfo") MultipartFile[] pics,GoodsInfo gf,HttpServletRequest request) {
		if(pics != null && pics.length > 0 && !pics[0].isEmpty()) {
			
			try {
				String savePath ="pics";
				
				String basepath = request.getServletContext().getRealPath("");//获取当前项目在服务器中的绝对路径
				String temp = request.getServletContext().getInitParameter("uploadPath");
				if(!StringUtil.checkNull(temp)) {
					savePath = temp;
				}
				String picStr = "";
				File dest = null;
				String path = null;
				
				for(MultipartFile fl : pics) {
					path = savePath + "/" + new Date().getTime() + "_" + fl.getOriginalFilename();
					dest = new File(new File(basepath).getParentFile(),path);
					//将图片存到本地服务器
					fl.transferTo(dest);
					picStr += path + ",";
				}
				if(!"".equals(picStr)) {
					picStr = picStr.substring(0,picStr.lastIndexOf(","));
				}
				gf.setPics(picStr);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int result = goodsInfoService.add(gf);
		if(result > 0) {
			return new ResultVO(ResultEnum.SUCCESS);
		}
		return new ResultVO(ResultEnum.ERROR);
	}
}
