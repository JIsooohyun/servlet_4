package com.sh.board.qna;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sh.action.Action;
import com.sh.action.ActionForward;
import com.sh.board.BoardDTO;
import com.sh.page.SearchMakePage;
import com.sh.page.SearchPager;
import com.sh.page.SearchRow;
import com.sh.upload.UploadDAO;
import com.sh.upload.UploadDTO;
import com.sh.util.DBConnector;

public class QnaService implements Action {
	private QnaDAO qnaDAO;
	private UploadDAO uploadDAO;

	public QnaService() {
		qnaDAO = new QnaDAO();
		uploadDAO = new UploadDAO();
	}

	@Override
	public ActionForward list(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		String path = "../WEB-INF/views/board/boardList.jsp";
		boolean check = true;

		int curPage = 1;
		try {
			curPage = Integer.parseInt(request.getParameter("curPage"));
		}catch (Exception e) {
		}
		String kind = request.getParameter("kind");
		String search = request.getParameter("search");

		SearchMakePage s = new SearchMakePage(curPage, kind, search);

		SearchRow searchRow = s.makeRow();
		int totalCount = 0;
		List<BoardDTO> ar = null;
		Connection conn = null;
		try {
			conn = DBConnector.getConnect();
			ar = qnaDAO.selectList(searchRow, conn);
			totalCount = qnaDAO.getTotalCount(searchRow, conn);
			SearchPager searchPager = s.makePage(totalCount);
			request.setAttribute("pager", searchPager);
			request.setAttribute("list", ar);
			request.setAttribute("board", "qna");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		actionForward.setPath(path);
		actionForward.setCheck(check);
		return actionForward;
	}

	@Override
	public ActionForward select(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		BoardDTO boardDTO = null;
		List<UploadDTO> ar = null;
		Connection conn=null;
		try {
			conn = DBConnector.getConnect();
			int num = Integer.parseInt(request.getParameter("num"));
			boardDTO = qnaDAO.selectOne(num, conn);
			ar = uploadDAO.selectList(num, conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String path="";
		if(boardDTO != null) {
			request.setAttribute("dto", boardDTO);
			request.setAttribute("upload", ar);
			path ="../WEB-INF/views/board/boardSelect.jsp";
		}else {
			request.setAttribute("msg", "No Data");
			request.setAttribute("path", "./noticeList");
			path="../WEB-INF/views/common/result.jsp";
		}
		//글이 있으면 출력
		//글이 없으면 삭제되었거나 없는 글입니다.(alert) 리스트로 
		actionForward.setCheck(true);
		actionForward.setPath(path);
		return actionForward;

	}

	@Override
	public ActionForward insert(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		String method = request.getMethod();
		actionForward.setCheck(true);
		actionForward.setPath("../WEB-INF/views/board/boardWrite.jsp");
		MultipartRequest multi =null;

		if(method.equals("POST")) {
			String saveDirectory = request.getServletContext().getRealPath("/upload");
			
			File file = new File(saveDirectory);
			
			if(!file.exists()) {//존재하지 않는다면 문제가 생긴다. 
				file.mkdirs();
			}
			int maxPostSize = 1024*1024*100;//1024는 1kb-> 지금 총 100mb
			String encoding = "utf-8";
			Connection conn = null;
			try {
				multi = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, new DefaultFileRenamePolicy()); //이 객체가 만들어지는 순간 파일 저장 끝

				Enumeration<String> e = multi.getFileNames();//파일의 파라미터 이름들 
				ArrayList<UploadDTO> ar = new ArrayList<UploadDTO>();
				while(e.hasMoreElements()) {
					String s =e.nextElement();
					String fname = multi.getFilesystemName(s);
					String oname = multi.getOriginalFileName(s);
					UploadDTO uploadDTO = new UploadDTO();
					uploadDTO.setFileName(fname);
					uploadDTO.setoName(oname);
					ar.add(uploadDTO);
				}//파일 이름이 유동적이라서 이걸 사용하게 됨
				QnaDTO qnaDTO = new QnaDTO();
				qnaDTO.setTitle(multi.getParameter("title"));
				qnaDTO.setWriter(multi.getParameter("writer"));
				qnaDTO.setContents(multi.getParameter("contents"));


				//1.시퀀스 번호
				int num = qnaDAO.getNum();
				qnaDTO.setNum(num);

				//2.insert
				conn = DBConnector.getConnect();
				conn.setAutoCommit(false); //트랜잭션처리를 위해서 사용
				num = qnaDAO.insert(qnaDTO, conn);

				//3.upload
				for(UploadDTO uploadDTO : ar) {
					uploadDTO.setNum(qnaDTO.getNum());
					num = uploadDAO.insert(uploadDTO, conn);
					if(num<1) {
						throw new Exception();
					}
				}
				conn.commit();

			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally {
				try {
					conn.setAutoCommit(true); //다른 사람들을 위해서 다시 autCommit을 해준다.
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			actionForward.setCheck(false);
			actionForward.setPath("./qnaList");

		}//post끝

		return actionForward;
	}

	@Override
	public ActionForward update(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();
		actionForward.setCheck(true);
		actionForward.setPath("../WEB-INF/views/board/boardUpdate.jsp");
		String method = request.getMethod();
		
		if(method.equals("POST")) {
			//MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
		}else {
			int num = Integer.parseInt(request.getParameter("num"));
			Connection conn=null;
			BoardDTO boardDTO = null;
			List<UploadDTO> ar = null;
			try {
				conn = DBConnector.getConnect();
				boardDTO = qnaDAO.selectOne(num, conn);
				ar = uploadDAO.selectList(num, conn);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//finally
			request.setAttribute("dto", boardDTO);
			request.setAttribute("upload", ar);
			
		}//GET
		
		
		return actionForward;
	}

	@Override
	public ActionForward delete(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();

		return actionForward;
	}

	public ActionForward reply(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();

		return actionForward;
	}
}
