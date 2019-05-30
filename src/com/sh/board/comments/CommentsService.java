package com.sh.board.comments;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sh.action.Action;
import com.sh.action.ActionForward;
import com.sh.page.SearchMakePage;
import com.sh.page.SearchPager;
import com.sh.page.SearchRow;
import com.sh.util.DBConnector;

import sun.security.pkcs11.Secmod.DbMode;

public class CommentsService implements Action {
	private CommentsDAO commentsDAO;

	public CommentsService() {
		commentsDAO = new CommentsDAO();
	}
	@Override
	public ActionForward list(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();

		int curPage = 1;
		int num=0;
		try {
			curPage = Integer.parseInt(request.getParameter("curPage"));
		}catch (Exception e) {
			// TODO: handle exception
		}
		try {
			num = Integer.parseInt(request.getParameter("num"));
		}catch (Exception e) {
			// TODO: handle exception
		}

		SearchMakePage s = new SearchMakePage(curPage, "", "");
		SearchRow searchRow = s.makeRow();
		List<CommentsDTO> ar = null;
		Connection conn = null;

		try {
			conn = DBConnector.getConnect();
			ar = commentsDAO.list(searchRow,num, conn);
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
		request.setAttribute("commentsList", ar);
		actionForward.setCheck(true);//request로 담아놨기 때문에 redirect로 가면 안된다. 
		actionForward.setPath("../WEB-INF/views/common/list.jsp");
		return actionForward;
	}

	@Override
	public ActionForward select(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();

		return actionForward;
	}

	@Override
	public ActionForward insert(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		String method = request.getMethod();
		Connection conn = null;
		CommentsDTO commentsDTO = null;
		int result = 0;
		try {
			conn = DBConnector.getConnect();
			commentsDTO = new CommentsDTO();
			commentsDTO.setNum(Integer.parseInt(request.getParameter("num")));
			commentsDTO.setContents(request.getParameter("contents"));
			commentsDTO.setWriter(request.getParameter("writer"));
			result = commentsDAO.insert(commentsDTO, conn);
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
		request.setAttribute("result", result);
		actionForward.setCheck(true);
		actionForward.setPath("../WEB-INF/views/common/result2.jsp");
		
		return actionForward;
	}

	@Override
	public ActionForward update(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		CommentsDTO commentsDTO = null;
		Connection conn = null;
		int result=0;
		try {
			conn = DBConnector.getConnect();
			commentsDTO = new CommentsDTO();
			commentsDTO.setCnum(Integer.parseInt(request.getParameter("cnum")));
			commentsDTO.setContents(request.getParameter("contents"));
			result = commentsDAO.update(commentsDTO, conn);
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
		request.setAttribute("result", result);
		actionForward.setCheck(true);
		actionForward.setPath("../WEB-INF/views/common/result2.jsp");
		return actionForward;
	}

	@Override
	public ActionForward delete(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		Connection conn =null;
		int result = 0;
		try {
			conn = DBConnector.getConnect();
			int cnum = Integer.parseInt(request.getParameter("cnum"));
			result = commentsDAO.delete(cnum, conn);
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
		request.setAttribute("result", result);
		actionForward.setCheck(true);
		actionForward.setPath("../WEB-INF/views/common/result2.jsp");
		return actionForward;
	}

}
