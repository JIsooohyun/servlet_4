package com.sh.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sh.action.ActionForward;
import com.sh.board.comments.CommentsService;

/**
 * Servlet implementation class CommentsController
 */
@WebServlet("/CommentsController")
public class CommentsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private CommentsService commentsService;
    public CommentsController() {
        super();
        commentsService = new CommentsService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getPathInfo();
		ActionForward actionForward = null;
		if(command.equals("/commentsWrite")) {
			actionForward = commentsService.insert(request, response);
		}else if(command.equals("/commentsDelete")) {
			actionForward = commentsService.delete(request, response);
		}else if(command.equals("/commentsUpadte")) {
			actionForward = commentsService.update(request, response);
		}else if(command.equals("/commentsList")) {
			actionForward = commentsService.list(request, response);
		}else {
			
		}
		
		if(actionForward.isCheck()) {
			RequestDispatcher view = request.getRequestDispatcher(actionForward.getPath());
			view.forward(request, response);
		}else {
			response.sendRedirect(actionForward.getPath());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
