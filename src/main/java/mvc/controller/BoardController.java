package mvc.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

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
			try {
				requestBoardWrite(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		
		// ajax로 리플처리
		else if (command.contains("RippleListAction.do")) {
			requestRippleList(request, response);
		}
		
		else if (command.contains("RippleWriteAction.do")) {
			requestRippleWrite(request, response);
		}
		
		else if (command.contains("RippleDeleteAction.do")) {	// 댓글 삭제
			requestRippleDelete(request, response);
		}
		
		else {
			System.out.println("out : " + command);
			// 결과 화면을 출력 스트림을 통해 출력
			PrintWriter out = response.getWriter();
			out.append("<html><body><h2>잘못된 경로입니다.(" + command + "</h2><hr>");
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
	public void requestBoardWrite(HttpServletRequest request) throws Exception {
		BoardDAO dao = BoardDAO.getInstance();
		
		BoardDTO board = new BoardDTO();
		
		HttpSession session = request.getSession();
		board.setId((String) session.getAttribute("sessionId"));
		
		// 폼 페이지에서 전송된 파일을 저장할 서버의 경롤르 작성
		String path=  "C:/img";
		
		// 파일 업로드를 위해 DiskFileUpload 클래스를 생성
		DiskFileUpload upload = new DiskFileUpload();
		
		// 업로드할 파일의 최대 크기, 메모리상에 저장할 최대 크기, 업로드된 파일을 임시로 저장할 경로를 작성
		upload.setSizeMax(1000000);
		upload.setSizeThreshold(4096);
		upload.setRepositoryPath(path);
		
		// 폼 페이지에서 전송된 요청 파라미터를 전달받도록 DiskFileUpload 객체 타입의 parseRequest() 메서드를 작성
		List items = upload.parseRequest(request);
		
		// 폼 페이지에서 전송된 요청 파라미터를 Iterator 클래스로 변환
		Iterator params = items.iterator();
		
		while (params.hasNext()) {	// 폼 페이지에서 전송된 요청 파라미터가 없을 때까지 반복하도록 Iterator 객체 타입의 hasNext() 메서드를 작성
			// 폼 페이지에서 전송된 요청 파라미터의 이름을 가져오도록 Iterator 객체 타입의 next() 메서드를 작성
			FileItem item = (FileItem) params.next();
			
			if (item.isFormField()) {
				// 폼 페이지에서 전송된 요청 파라미터가 일반 데이터이면 요청 파라미터의 이름과 값을 출력
				String name = item.getFieldName();
				String value = item.getString("utf-8");
				
				switch (name) {
					case "name":
						board.setName(value);
						break;
					case "subject":
						board.setSubject(value);
						break;
					case "content":
						board.setContent(value);
						break;
				}
				System.out.println(name + "=" + value + "<br>");
			}
			else {
				// 폼 페이지에서 전송된 요청 파라미터가 파일이면
				// 요청 파라미터의 이름, 저장 파일의 이름, 파일 컨텐츠 유형, 파일 크기에 대한 정보를 출력
				String fileFieldName = item.getFieldName();
				String fileName = item.getName();
				String contentType = item.getContentType();
				
				if (!fileName.isEmpty()) {
					System.out.println("파일이름 : " + fileName);
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					long fileSize = item.getSize();
					
					File file = new File(path + "/" + fileName);
					item.write(file);
					
					board.setFilename(fileName);
					board.setFilesize(fileSize);
					
					System.out.println("----------------------------<br>");
					System.out.println("요청 파라미터 이름 : " + fileFieldName + "<br>");
					System.out.println("저장 파일 이름 : " + fileName + "<br>");
					System.out.println("파일 콘텐츠 타입 : " + contentType + "<br>");
					System.out.println("파일 크기 : " + fileSize);
				}
			}
		}

		//java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd(HH:mm:ss)");
		//String regist_day = formatter.format(new java.util.Date());
		
		board.setHit(0);
		//board.setRegist_day(regist_day);
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
		ripple.setBoardName(this.boardName);
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
	
	
	public void requestRippleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("sessionId");
		
		String boardName = request.getParameter("boardName");
		int num = Integer.parseInt(request.getParameter("num"));
		
		RippleDAO dao = RippleDAO.getInstance();
		ArrayList<RippleDTO> list = dao.getRippleList(boardName, num);
		
		StringBuilder result = new StringBuilder("{ \"listData\" :  [");
		int i = 0;
		for (RippleDTO dto : list) {
			boolean flag = sessionId != null && sessionId.equals(dto.getMemberId()) ? true : false;
			result.append("{\"rippleId\" : \"")
			.append(dto.getRippleId())
			.append("\", \"name\" : \"")
			.append(dto.getName())
			.append("\", \"content\" : \"")
			.append(dto.getContent())
			.append("\", \"isWriter\": \"")
			.append(flag)
			.append("\" }");
			// value가 배열 형태로 들어가서 마지막 요소의 경우에는 콤마가 나오면 안됨
			
			if (i++ < list.size() - 1)
				result.append(", ");
		}
		result.append("]}");
		
		// 결과 화면을 출력 스트림을 통해 출력
		PrintWriter out = response.getWriter();
		out.append(result.toString());
	}
	
	public void requestRippleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rippleId = Integer.parseInt(request.getParameter("rippleId"));
		RippleDAO dao = RippleDAO.getInstance();
		RippleDTO ripple = new RippleDTO();
		ripple.setRippleId(rippleId);
		
		String result = "{ \"result\" : ";
		if (dao.deleteRipple(ripple)) {
			result += "\"true\"}";
		}
		else {
			result += "\"false\"}";
		}
		// 결과 화면을 출력 스트림을 통해 출력
		PrintWriter out = response.getWriter();
		out.append(result);
		
	}
	
	public void requestRippleWrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		RippleDAO dao = RippleDAO.getInstance();
		RippleDTO ripple = new RippleDTO();
		HttpSession session = request.getSession();
		
		request.setCharacterEncoding("utf-8");
		
		ripple.setBoardName(request.getParameter("boardName"));
		ripple.setBoardNum(Integer.parseInt(request.getParameter("num")));
		ripple.setMemberId((String) session.getAttribute("sessionId"));
		ripple.setName(request.getParameter("name"));
		ripple.setContent(request.getParameter("content"));
		ripple.setIp(request.getRemoteAddr());
		
		String result = "{ \"result\" : ";
		if (dao.insertRipple(ripple)) {
			result += "\"true\"}";
		}
		else {
			result += "\"false\"}";
		}
		// 결과 화면을 출력 스트림을 통해 출력
		PrintWriter out = response.getWriter();
		out.append(result);
		
	}
	
}
