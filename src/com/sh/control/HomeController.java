package com.sh.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sh.member.MemberDTO;

/**
 * Servlet implementation class HomeContriller
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s = request.getParameter("s");
		if(s!=null &&s.equals("2")) {
			MemberDTO memberDTO = new MemberDTO(); //세션에 memberDTO만 넣어주면 된다.
			memberDTO.setId("admin");
			request.getSession().setAttribute("memberDTO", memberDTO);
		}else if(s!=null && s.equals("1")) {
			MemberDTO memberDTO = new MemberDTO(); //세션에 memberDTO만 넣어주면 된다.
			memberDTO.setId("iu");
			request.getSession().setAttribute("memberDTO", memberDTO);
		}else if(s!=null && s.equals("3")){
			request.getSession().invalidate();
		}
		
		RequestDispatcher view = request.getRequestDispatcher("./WEB-INF/views/index.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
