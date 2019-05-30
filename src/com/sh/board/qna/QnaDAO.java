package com.sh.board.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sh.board.BoardDAO;
import com.sh.board.BoardDTO;
import com.sh.page.SearchRow;
import com.sh.upload.UploadDTO;
import com.sh.util.DBConnector;

public class QnaDAO implements BoardDAO {

	@Override
	public int getNum() throws Exception {
		int result=0;
		Connection con = DBConnector.getConnect();
		String sql ="select qna_seq.nextval from dual";
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
		int result=0;
		
		String sql ="select count(num) from qna where "+searchRow.getSearch().getKind()+" like ?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, "%"+searchRow.getSearch().getSearch()+"%");
		ResultSet rs = st.executeQuery();
		rs.next();
		result = rs.getInt(1);
		rs.close();
		st.close();
		return result;
	}

	@Override
	public int insert(BoardDTO boardDTO, Connection conn) throws Exception {
		int result=0;
		//String sql = "insert into qna values(?, ?, ?, ?, sysdate, 0, ?, 0, 0)";//시퀀스번호를 여기다가 쓰면 안된다. 
//		String sql ="insert into qna values(?,?,?,?,sysdate,0,?,0,0)";
//		PreparedStatement st = conn.prepareStatement(sql);
//		st.setInt(1, boardDTO.getNum());
//		st.setString(2, boardDTO.getTitle());
//		st.setString(3, boardDTO.getContents());
//		st.setString(4, boardDTO.getWriter());
//		st.setInt(5, boardDTO.getNum());
//		result = st.executeUpdate();
//		st.close();
		String sql ="insert into qna values(?,?,?,?,sysdate,0,?,0,0)";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, boardDTO.getNum());
		st.setString(2, boardDTO.getTitle());
		st.setString(3, boardDTO.getContents());
		st.setString(4, boardDTO.getWriter());
		st.setInt(5, boardDTO.getNum());
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
		QnaDTO qnaDTO = null;
		String sql ="select * from qna where num=?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, num);
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			qnaDTO = new QnaDTO();
			qnaDTO.setNum(rs.getInt("num"));
			qnaDTO.setTitle(rs.getString("title"));
			qnaDTO.setContents(rs.getString("contents"));
			qnaDTO.setWriter(rs.getString("writer"));
			qnaDTO.setReg_date(rs.getString("reg_date"));
			qnaDTO.setHit(rs.getInt("hit"));
		}
		rs.close();
		st.close();
		return qnaDTO;
	}

	@Override
	public List<BoardDTO> selectList(SearchRow searchRow, Connection conn) throws Exception {
		ArrayList<BoardDTO> ar = new ArrayList<BoardDTO>();
		QnaDTO qnaDTO = null;
		String sql = "select * from "
				+"(select rownum R, q.* from "
				+"(select * from qna where "+searchRow.getSearch().getKind()+" like ? order by ref desc, step asc) q) "
				+"where R between ? and ?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, "%"+searchRow.getSearch().getSearch()+"%");
		st.setInt(2, searchRow.getStartRow());
		st.setInt(3, searchRow.getLastRow());
		
		ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			qnaDTO = new QnaDTO();
			qnaDTO.setNum(rs.getInt("num"));
			qnaDTO.setTitle(rs.getString("title"));
			qnaDTO.setContents(rs.getString("contents"));
			qnaDTO.setWriter(rs.getString("writer"));
			qnaDTO.setReg_date(rs.getString("reg_date"));
			qnaDTO.setHit(rs.getInt("hit"));
			qnaDTO.setRef(rs.getInt("ref"));
			qnaDTO.setStep(rs.getInt("step"));
			qnaDTO.setDepth(rs.getInt("depth"));
			ar.add(qnaDTO);
		}
		rs.close();
		st.close();
		return ar;
	}
	
	public int reply(QnaDTO qnaDTO)throws Exception{
		int result=0;
		
		return result;
	}

}
