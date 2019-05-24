package com.sh.board.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sh.board.BoardDAO;
import com.sh.board.BoardDTO;
import com.sh.page.SearchRow;
import com.sh.util.DBConnector;

public class NoticeDAO implements BoardDAO{

	@Override
	public int getNum() throws Exception {
		int result=0;
		Connection con = DBConnector.getConnect();
		String sql ="select notice_seq.nextval from dual";
		PreparedStatement st =con.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		rs.next();
		result=rs.getInt(1);
		rs.close();
		st.close();
		return result;
	}

	@Override
	public int getTotalCount(SearchRow searchRow, Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(BoardDTO boardDTO, Connection conn) throws Exception {
		int result=0;
		
		String sql ="insert into notice values(?,?,?,?, sysdate,0)";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, boardDTO.getNum());
		st.setString(2, boardDTO.getTitle());
		st.setString(3, boardDTO.getContents());
		st.setString(4, boardDTO.getWriter());
		result = st.executeUpdate();
		st.close();
		return result;
	}

	@Override
	public int update(BoardDTO boardDTO, Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int num, Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BoardDTO selectOne(int num, Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardDTO> selectList(SearchRow searchRow, Connection conn) throws Exception {
		ArrayList<BoardDTO> ar = new ArrayList<BoardDTO>();
		NoticeDTO noticeDTO = null;
		String sql = "select * from "
					+"(select rownum R, p.* from "
					+"(select * from notice where "+searchRow.getSearch().getKind()+" like ? order by num desc) p) "
					+"where R between ? and ?";
		
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, "%"+searchRow.getSearch().getSearch()+"%");
		st.setInt(2, searchRow.getStartRow());
		st.setInt(3, searchRow.getLastRow());
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			noticeDTO = new NoticeDTO();
			noticeDTO.setNum(rs.getInt("num"));
			noticeDTO.setTitle(rs.getString("title"));
			noticeDTO.setContents(rs.getString("contents"));
			noticeDTO.setWriter(rs.getString("writer"));
			noticeDTO.setReg_date(rs.getString("reg_date"));
			noticeDTO.setHit(rs.getInt("hit"));
			ar.add(noticeDTO);
		}
		rs.close();
		st.close();
		
		return ar;
	}

}
