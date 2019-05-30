package com.sh.json;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sh.board.BoardDTO;
import com.sh.board.comments.CommentsDAO;
import com.sh.board.comments.CommentsDTO;
import com.sh.board.notice.NoticeDAO;
import com.sh.page.SearchMakePage;
import com.sh.page.SearchRow;
import com.sh.util.DBConnector;


public class JsonService {

	public void test2(HttpServletRequest request, HttpServletResponse response){
		CommentsDAO commentsDAO = new CommentsDAO();
		int num = Integer.parseInt(request.getParameter("num"));
		SearchMakePage s = new SearchMakePage(1, "", "");
		SearchRow searchRow = s.makeRow();
		Connection conn = null;
		List<CommentsDTO> ar = null;
		try {
			conn = DBConnector.getConnect();
			ar = commentsDAO.list(searchRow, num, conn);
			ar = commentsDAO.list(searchRow, num, conn);
			JSONArray jsonar = new JSONArray();//[]
			for(CommentsDTO commentsDTO:ar) {
				JSONObject jsonObject = new JSONObject();//{}
				jsonObject.put("writer", commentsDTO.getWriter());
				jsonObject.put("contents", commentsDTO.getContents());
				jsonar.add(jsonObject);
			}
			
			JSONObject js = new JSONObject();
			js.put("ar", jsonar);
			PrintWriter out = response.getWriter();
			out.println(js.toJSONString());
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void test1(HttpServletRequest request, HttpServletResponse response) {
		//this.test2(request, response);
		int num = Integer.parseInt(request.getParameter("num"));
		NoticeDAO noticeDAO = new NoticeDAO();
		Connection conn=null;
		try {
			conn = DBConnector.getConnect();
			BoardDTO boardDTO = noticeDAO.selectOne(num, conn);
			JSONObject jsonObject = new JSONObject();  //{} <-이게 만들어진겨
			System.out.println(boardDTO);
			jsonObject.put("writer", boardDTO.getWriter()); //{"writer":"test"}
			jsonObject.put("contents", boardDTO.getContents()); //{"writer":"test", "contents" : "contents"}
			jsonObject.put("title", boardDTO.getTitle()); //{"writer":"test", "contents" : "contents", "title" : "title"}
			
			PrintWriter out = response.getWriter();
			out.println(jsonObject.toJSONString()); //json형태로 만들어서 보낸다. 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
