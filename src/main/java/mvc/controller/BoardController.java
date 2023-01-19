package mvc.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.model.BoardDAO;
import mvc.model.BoardDTO;
import mvc.model.RippleDAO;
import mvc.model.RippleDTO;

@WebServlet("*.do")
public class BoardController extends HttpServlet {
	static final int LISTCOUNT = 5;	//
	private String boardName = "board";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);	//
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String RequestURI = request.getRequestURI();	// 
		String contextPath = request.getContextPath();	// 
		String command = RequestURI.substring(contextPath.length());	//
		
		response.setContentType("text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		System.out.println(command);
		
		if (command.contains("/BoardListAction.do")) {	// 
			requestBoardList(request);
			RequestDispatcher rd = request.getRequestDispatcher("../board/list.jsp");
			rd.forward(request,response);
		}
		else if (command.contains("/BoardWriteForm.do")) {	// 
			//requestLoginName(request);
			RequestDispatcher rd = request.getRequestDispatcher("../board/writeForm.jsp");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardWriteAction.do")) {	
			requestBoardWrite(request);
			RequestDispatcher rd = request.getRequestDispatcher("../board/BoardListAction.do");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardViewAction.do")) {		// 선택된 글 상세 페이지 가져오기
			requestBoardView(request);
			requestRippleList(request);
			RequestDispatcher rd = request.getRequestDispatcher("../board/BoardView.do");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardView.do")) {		// 글 상세 페이지 출력
			RequestDispatcher rd = request.getRequestDispatcher("../board/view.jsp");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardUpdateForm.do")) {		// 글 수정폼 출력
			requestBoardView(request);
			RequestDispatcher rd = request.getRequestDispatcher("../board/updateForm.jsp");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardUpdateAction.do")) {	// 글 수정
			requestBoardUpdate(request);
			RequestDispatcher rd = request.getRequestDispatcher("../board/BoardListAction.do");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardDeleteAction.do")) {	// // 선택된 글 삭제
			requestBoardDelete(request);	
			RequestDispatcher rd = request.getRequestDispatcher("../board/BoardListAction.do");
			rd.forward(request, response);
		}
		else if (command.contains("/BoardRippleWriteAction.do")) {	// 댓글 작성
			requestBoardRippleWrite(request);
			String num = request.getParameter("num");
			String pageNum = request.getParameter("pageNum");
			response.sendRedirect("BoardViewAction.do?num=" + num + "&pageNum=" + pageNum);
		}
		
		else if (command.contains("/BoardRippleDeleteAction.do")) {	// 댓글 삭제
			requestBoardRippleDelete(request);
			String num = request.getParameter("num");
			String pageNum = request.getParameter("pageNum");
			response.sendRedirect("BoardViewAction.do?num=" + num + "&pageNum=" + pageNum);
		}
	}
	
	// 인증된 사용자만 가져오기
	/*
	public void requestLoginName(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		BoardDAO dao = BoardDAO.getInstance();
		
		String name = dao.getLoginNameById(id);
		
		request.setAttribute("name", name);
	}
	*/
	
	// 
	public void requestBoardWrite(HttpServletRequest request) {
		BoardDAO dao = BoardDAO.getInstance();
		
		BoardDTO board = new BoardDTO();
		board.setId(request.getParameter("id"));
		board.setName(request.getParameter("name"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		
		// 
		System.out.println(request.getParameter("name"));
		System.out.println(request.getParameter("subject"));
		System.out.println(request.getParameter("content"));
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd(HH:mm:ss)");
		String regist_day = formatter.format(new java.util.Date());
		
		board.setHit(0);
		board.setRegist_day(regist_day);
		board.setIp(request.getRemoteAddr());
		
		dao.insertBoard(board);	
	}
	
	//
	public void requestBoardView(HttpServletRequest request) {
		BoardDAO dao = BoardDAO.getInstance();
		int num = Integer.parseInt(request.getParameter("num"));
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		
		BoardDTO board = new BoardDTO();
		board = dao.getBoardByNum(num, pageNum);
		
		request.setAttribute("num", num);
		request.setAttribute("page", pageNum);
		request.setAttribute("board", board);
	}
	
	// 
	public void requestBoardUpdate(HttpServletRequest request) {
		int num = Integer.parseInt(request.getParameter("num"));
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		
		BoardDAO dao = BoardDAO.getInstance();
		
		BoardDTO board = new BoardDTO();
		board.setNum(num);
		board.setName(request.getParameter("name"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		
		dao.updateBoard(board);
	}
	
	// 
	public void requestBoardDelete(HttpServletRequest request) {
		int num = Integer.parseInt(request.getParameter("num"));
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.deleteBoard(num);
	}
	
	// 
	public void requestBoardList(HttpServletRequest request) {
		BoardDAO dao = BoardDAO.getInstance();
		List<BoardDTO> boardlist = new ArrayList<BoardDTO>();
		
		int pageNum = 1;	// 
		int limit=LISTCOUNT;	// 
		
		if(request.getParameter("pageNum")!=null)	//
			pageNum=Integer.parseInt(request.getParameter("pageNum"));
		
		String items = request.getParameter("items");	// 
		String text = request.getParameter("text");		//
		
		int total_record = dao.getListCount(items, text);	//
		boardlist = dao.getBoardList(pageNum, limit, items, text);	// 
		
		int total_page;
		
		if (total_record % limit == 0) {	// 
			total_page = total_record/limit;
			Math.floor(total_page);
		}
		else {
			total_page = total_record/limit;
			Math.floor(total_page);
			total_page = total_page + 1;
		}
		
		request.setAttribute("limit", limit);
		request.setAttribute("pageNum", pageNum);	// 
		request.setAttribute("total_page", total_page);	// 
		request.setAttribute("total_record", total_record);	// 
		request.setAttribute("boardlist", boardlist);
	}
	
	// 댓글 작성
	public void requestBoardRippleWrite(HttpServletRequest request) throws UnsupportedEncodingException {
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		RippleDAO dao = RippleDAO.getInstance();
		RippleDTO ripple = new RippleDTO();
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		ripple.setBoardname(this.boardName);
		ripple.setBoardNum(num);
		ripple.setMemberId((String) session.getAttribute("sessionId"));
		ripple.setName(request.getParameter("name"));
		ripple.setContent(request.getParameter("content"));
		ripple.setIp(request.getRemoteAddr());
		
		dao.insertRipple(ripple);
	}
	
	// 댓글 목록 가져오기
	public void requestRippleList(HttpServletRequest request) {
		RippleDAO dao = RippleDAO.getInstance();
		List<RippleDTO> rippleList = new ArrayList<>();
		int num = Integer.parseInt(request.getParameter("num"));
		
		rippleList = dao.getRippleList(this.boardName, num);
		
		request.setAttribute("rippleList", rippleList);
	}
	
	// 댓글 삭제
	public void requestBoardRippleDelete(HttpServletRequest request) throws UnsupportedEncodingException {
		
		int rippleId = Integer.parseInt(request.getParameter("rippleId"));
		
		RippleDAO dao = RippleDAO.getInstance();
		RippleDTO ripple = new RippleDTO();
		ripple.setRippleId(rippleId);
		dao.deleteRipple(ripple);
	}
	
}
