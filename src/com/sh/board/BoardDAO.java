package com.sh.board;

import java.sql.Connection;
import java.util.List;

import com.sh.page.SearchRow;

public interface BoardDAO {
	//getNum
	public int getNum() throws Exception;
	
	//getTotalCount
	public int getTotalCount(SearchRow searchRow, Connection conn) throws Exception;
	
	//insert
	public int insert(BoardDTO boardDTO, Connection conn)throws Exception;
	
	//update
	public int update(BoardDTO boardDTO, Connection conn)throws Exception;
	
	//delete
	public int delete(int num, Connection conn)throws Exception;
	
	//selectOne
	public BoardDTO selectOne(int num, Connection conn)throws Exception;
	
	//selectList
	public List<BoardDTO> selectList(SearchRow searchRow, Connection conn)throws Exception;
	
}
