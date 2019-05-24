package com.sh.board.notice;

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

public class NoticeService implements Action {
	private NoticeDAO noticeDAO;
	private UploadDAO uploadDAO;

	public NoticeService() {
		noticeDAO = new NoticeDAO();
		uploadDAO = new UploadDAO();
	}

	@Override
	public ActionForward list(HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = new ActionForward();

		int curPage = 1;
		try {
			curPage = Integer.parseInt(request.getParameter("curPage"));
		}catch (Exception e) {
			//예외가 발생하면 curPage는 1로 내려온다. 
		}
		String kind = request.getParameter("kind");

		String search = request.getParameter("search");

		//메서드 호출
		SearchMakePage s = new SearchMakePage(curPage, kind, search);

		//1.row
		SearchRow searchRow = s.makeRow();
		List<BoardDTO> ar = null;
		Connection conn=null;
		try {
			conn = DBConnector.getConnect();
			ar = noticeDAO.selectList(searchRow, conn);
			//2. page
			int totalCount =noticeDAO.getTotalCount(searchRow, conn);
			SearchPager searchPager = s.makePage(totalCount);

			request.setAttribute("pager", searchPager);
			request.setAttribute("list", ar);
			request.setAttribute("board", "notice");
			actionForward.setCheck(true); //포워드로 보내겠다.
			actionForward.setPath("../WEB-INF/views/board/boardList.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("msg", "Server Error");
			request.setAttribute("path", "../index.do");
			actionForward.setCheck(true);
			actionForward.setPath("../WEB-INF/views/common/result.jsp");
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return actionForward;
	}

	@Override
	public ActionForward select(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
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
			BoardDTO boardDTO =null;
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
				NoticeDTO noticeDTO = new NoticeDTO();
				noticeDTO.setTitle(multi.getParameter("title"));
				noticeDTO.setWriter(multi.getParameter("writer"));
				noticeDTO.setContents(multi.getParameter("contents"));

				conn = DBConnector.getConnect();
				//1.시퀀스 번호
				int num = noticeDAO.getNum();
				noticeDTO.setNum(num);

				//2.insert
				conn.setAutoCommit(false); //트랜잭션처리를 위해서 사용
				num = noticeDAO.insert(noticeDTO, conn);

				//3.upload
				for(UploadDTO uploadDTO : ar) {
					uploadDTO.setNum(noticeDTO.getNum());
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
			actionForward.setPath("./noticeList");

		}//post끝

		return actionForward;
	}

	@Override
	public ActionForward update(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionForward delete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
