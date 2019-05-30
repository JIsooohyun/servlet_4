package com.sh.filter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sh.member.MemberDTO;

/**
 * Servlet Filter implementation class FilterTest1
 */
@WebFilter("/FilterTest1")
public class FilterTest1 implements Filter {
	private HashMap<String, Boolean> map;
    /**
     * Default constructor. 
     */
    public FilterTest1() {
    	map = new HashMap<String, Boolean>();
    	map.put("/noticeList", false);
    	map.put("/noticeSelect", false);
    	map.put("/noticeWrite", true);
    	map.put("/noticeUpdate", true);
    	map.put("/noticeDelete", true);
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() { //필터가 없어질 때 
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	//HttpServletRequest의 ServletRequest은 부모 형태 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		//request부분
		
		//부모형태가 아닌 자식형태로 형변환
		HttpSession session = ((HttpServletRequest)request).getSession();
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("memberDTO");
		String command = ((HttpServletRequest)request).getPathInfo();
		boolean check = map.get(command);
		if(check) {
			//관리자만 통과할 수 있ㄷ록
			if(memberDTO!=null && memberDTO.getId().equals("admin")) {
				chain.doFilter(request, response);
			}else {
				((HttpServletResponse)response).sendRedirect("../index.do");
			}
		}else {
			//아니면 다음 컨트롤러로 보내기
			chain.doFilter(request, response);
		}
		
		//너가 요청하는게 무엇이냐 -> 거르기
		//System.out.println("Filter In");
		//chain.doFilter(request, response);//필터가 있으면 다음 필터로 보내거나 없으면 컨트롤러로 보내라. 
		//response부분
		//System.out.println("Filter Out");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}//초기화메서드 

}
