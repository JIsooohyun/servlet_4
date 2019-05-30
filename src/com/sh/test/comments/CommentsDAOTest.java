package com.sh.test.comments;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sh.board.comments.CommentsDAO;
import com.sh.board.comments.CommentsDTO;
import com.sh.util.DBConnector;

public class CommentsDAOTest {
	private CommentsDAO commentsDAO;
	private Connection conn;
	
	public CommentsDAOTest() {
		commentsDAO = new CommentsDAO();
	}
	
	@BeforeClass
	public static void start() {
		
	}//맨 처음에 딱 한번 하는 메서드
	
	@Before
	public void s() {
		try {
			conn = DBConnector.getConnect();
			//conn.setAutoCommit(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void InsertTest()throws Exception {
		CommentsDTO commentsDTO = new CommentsDTO();
		commentsDTO.setNum(21);
		commentsDTO.setWriter("iu");
		commentsDTO.setContents("contents");
		int result = commentsDAO.insert(commentsDTO, conn);
		assertEquals(1, result);//같아야지 성공
		
	}
	
	
	@After
	public void a() {
		try {
			//conn.rollback();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass 
	public static void after() {
		
	}//맨 마지막에 딱 한번 하는 메서드

}
