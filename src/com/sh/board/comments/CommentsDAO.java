package com.sh.board.comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sh.page.SearchRow;

public class CommentsDAO {
	
	public int getTotalCount(SearchRow searchRow, Connection conn)throws Exception{
		int result = 0;
		String sql = "select count(num) from comments where "+searchRow.getSearch().getKind()+" like ?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, "%"+searchRow.getSearch().getSearch()+"%");
		ResultSet rs = st.executeQuery();
		rs.next();
		result = rs.getInt(1);
		rs.close();
		st.close();
		return result;
	}
	public List<CommentsDTO> list(SearchRow searchRow, int num, Connection conn)throws Exception{
		List<CommentsDTO> ar = new ArrayList<CommentsDTO>();
		CommentsDTO commentsDTO = null;
		String sql = "select * from "
				+"(select rownum R, C.* from "
				+"(select * from comments where num =? order by cnum desc) C) "
				+"where R between ? and ?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, num);
		st.setInt(2, searchRow.getStartRow());
		st.setInt(3, searchRow.getLastRow());
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			commentsDTO = new CommentsDTO();
			commentsDTO.setCnum(rs.getInt("cnum"));
			commentsDTO.setNum(rs.getInt("num"));
			commentsDTO.setWriter(rs.getString("writer"));
			commentsDTO.setContents(rs.getString("contents"));
			commentsDTO.setReg_date(rs.getString("reg_date"));
			ar.add(commentsDTO);
		}
		st.close();
		rs.close();
		return ar;
	}
	
	public int insert(CommentsDTO commentsDTO, Connection conn) throws Exception {
		int result=0;
		String sql = "insert into comments values(QNA_SEQ.nextval, ?, ?, ?, sysdate)";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, commentsDTO.getNum());
		st.setString(2, commentsDTO.getWriter());
		st.setString(3, commentsDTO.getContents());
		result  = st.executeUpdate();
		st.close();
		return result;
	}

	public int delete(int cnum, Connection conn)throws Exception{
		int result=0;
		String sql = "delete from comments where cnum=?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, cnum);
		result = st.executeUpdate();
		st.close();
		return result;
	}
	
	public int update(CommentsDTO commentsDTO, Connection conn)throws Exception{
		int result=0;
		String sql = "update comments set contents=? where cnum=?";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, commentsDTO.getContents());
		st.setInt(2, commentsDTO.getCnum());
		result = st.executeUpdate();
		st.close();
		return result;
	}
}
