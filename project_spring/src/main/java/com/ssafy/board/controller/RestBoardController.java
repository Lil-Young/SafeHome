package com.ssafy.board.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.board.model.BoardDetailDto;
import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.CommentDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.member.model.MemberDto;
import com.ssafy.util.PageNavigation;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/article")
public class RestBoardController {
//	@Autowired
//	private ServletContext servletContext;

	private final BoardService boardService;

	public RestBoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	/*
	 * 글 작성
	 */
	@PostMapping("/write")
	public ResponseEntity<?> write(@RequestBody BoardDto boardDto, HttpSession session
	/* ,@RequestBody Map<String, String> map */) throws Exception {
		System.out.println(boardDto.toString());
		System.out.println("@@@@@@data :" + boardDto.getArticleNo());
		int cnt = boardService.writeArticle(boardDto);
		System.out.println("cnt : " + cnt);
		return ResponseEntity.ok(cnt);
	}

	/*
	 * 글 리스트 가져오기
	 */
	@GetMapping("/list")
	public ResponseEntity<?> list(@RequestParam Map<String, String> map) throws Exception {
		System.out.println("data 도착");
		System.out.println(map.get("pgno"));
		List<BoardDto> list = boardService.listArticle(map);
		PageNavigation pageNavigation = boardService.makePageNavigation(map);
		Map<String, Object> response = new HashMap<>();
		response.put("articles", list);
		return ResponseEntity.ok(response);
	}

	/*
	 * 특정 글 가져오기
	 */
	@PostMapping("/view")
	public ResponseEntity<?> view(@RequestBody Map<String, String> map) throws Exception {
		System.out.println("view 도착 : " + map);
		
		int articleNo = Integer.parseInt(map.get("articleno"));
		BoardDetailDto boardDetailDto = boardService.getArticle(articleNo);
		System.out.println(boardDetailDto);
		boardService.updateHit(articleNo);
		Map<String, Object> response = new HashMap<>();
		response.put("article", boardDetailDto);
		System.out.println("response : " + response.get("article"));
		return ResponseEntity.ok(response);
	}

	
	@PostMapping("/addComment")
	public ResponseEntity<?> addComment(@RequestBody CommentDto commentDto) throws Exception{
		System.out.println("addComment 도착@@@@@@" + commentDto);
		int cnt =boardService.addComment(commentDto);
		if(cnt >0) {
			return ResponseEntity.ok("success");			
		}
		return ResponseEntity.ok("fail");
		
	}
	
//	@GetMapping("/modify")
//	public String modify(
//			@RequestParam("articleno") int articleNo, 
//			@RequestParam Map<String, String> map, 
//			Model model)
//			throws Exception {
//		return "board/modify";
//	}

	@GetMapping("/getModifyData")
	public ResponseEntity<?> getModifyData(@RequestParam int articleNo) throws Exception{
		BoardDto boardDto = boardService.getModifyData(articleNo);
		System.out.println(boardDto);
		return ResponseEntity.ok(boardDto);
	}
	
	
	
	/*
	 * 글 수정하기
	 */
	@PutMapping("/modify")
	public ResponseEntity<String> modify(@RequestBody BoardDto boardDto) throws Exception {
//		int articleNo = Integer.parseInt(map.get("articleno"));
//		BoardDto boardDto = boardService.getArticle(articleNo);
		boardService.modifyArticle(boardDto);
		return ResponseEntity.ok("글 수정 완료");
	}

	/*
	 * 글 삭제하기
	 */
	@DeleteMapping("/delete/{articleno}")
	public ResponseEntity<String> delete(@PathVariable("articleno") int articleNo) throws Exception {
		boardService.deleteArticle(articleNo);
//		redirectAttributes.addAttribute("pgno", map.get("pgno"));
//		redirectAttributes.addAttribute("key", map.get("key"));
//		redirectAttributes.addAttribute("word", map.get("word"));
		return ResponseEntity.ok("글 삭제 완료");
	}
	
	@GetMapping("searchAuthor")
	public ResponseEntity<?> searchAuthor(@RequestParam String term) throws Exception{
		List<BoardDto> list = boardService.searchAuthor(term);
		System.out.println(list);
		Map<String, Object> response = new HashMap<>();
		response.put("articles", list);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("searchTitle")
	public ResponseEntity<?> searchTitle(@RequestParam String term) throws Exception{
		List<BoardDto>list = boardService.searchTitle(term);
		System.out.println(list);
		Map<String, Object> response = new HashMap<>();
		response.put("articles", list);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("searchAll")
	public ResponseEntity<?> searchAll(@RequestParam String term) throws Exception{
		List<BoardDto> list = boardService.searchAll(term);
		System.out.println(list);
		Map<String, Object> response = new HashMap<>();
		response.put("articles", list);
		return ResponseEntity.ok(response);
	}
	
	
	
	
}
